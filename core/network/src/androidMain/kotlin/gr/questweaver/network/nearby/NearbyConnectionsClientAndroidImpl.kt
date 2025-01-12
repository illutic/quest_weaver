package gr.questweaver.network.nearby

import android.content.Context
import androidx.core.net.toUri
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.AdvertisingOptions
import com.google.android.gms.nearby.connection.ConnectionInfo
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback
import com.google.android.gms.nearby.connection.ConnectionResolution
import com.google.android.gms.nearby.connection.ConnectionsClient
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo
import com.google.android.gms.nearby.connection.DiscoveryOptions
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback
import com.google.android.gms.nearby.connection.PayloadCallback
import com.google.android.gms.nearby.connection.PayloadTransferUpdate
import com.google.android.gms.nearby.connection.Strategy
import gr.questweaver.model.Device
import gr.questweaver.model.DeviceState
import gr.questweaver.network.NearbyConnectionsClient
import gr.questweaver.network.model.Message
import gr.questweaver.network.model.Payload
import gr.questweaver.network.model.metadataOrNull
import gr.questweaver.network.model.toDomain
import gr.questweaver.network.model.toNetworkDto
import gr.questweaver.network.serializer.toByteArray
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private typealias NearbyApiPayload = com.google.android.gms.nearby.connection.Payload
private typealias NearbyApiPayloadType = com.google.android.gms.nearby.connection.Payload.Type

internal class NearbyConnectionsClientAndroidImpl(
    ioDispatcher: CoroutineDispatcher,
    private val context: Context,
    private val serviceId: String,
) : NearbyConnectionsClient,
    CoroutineScope by CoroutineScope(SupervisorJob() + ioDispatcher) {
    private val incomingPayloadsChannel = Channel<Payload>()
    private val client: ConnectionsClient = Nearby.getConnectionsClient(context)
    private val callbackJob = CoroutineName("Callback") + coroutineContext

    private val outgoingPayloads = mutableSetOf<Long>()
    override val incomingPayloads: Flow<Any> =
        incomingPayloadsChannel
            .receiveAsFlow()
            .filterIsInstance<Message>()
            .mapNotNull { it.dto.toDomain() }
            .stateIn(scope = this, started = SharingStarted.Companion.Eagerly, initialValue = null)
            .filterNotNull()

    override fun startDiscovery(): Flow<Set<Device>> =
        callbackFlow {
            val devicesFound = mutableSetOf<Device>()
            val discoveryOptions =
                DiscoveryOptions
                    .Builder()
                    .setStrategy(Strategy.P2P_STAR)
                    .build()

            val callback =
                object : EndpointDiscoveryCallback() {
                    override fun onEndpointFound(
                        endpointId: String,
                        info: DiscoveredEndpointInfo,
                    ) {
                        val device =
                            Device(
                                id = endpointId,
                                name = info.endpointName,
                                state = DeviceState.Connecting,
                            )
                        devicesFound.add(device)

                        trySend(devicesFound)
                    }

                    override fun onEndpointLost(endpointId: String) {
                        devicesFound.removeIf { it.id == endpointId }
                        trySend(devicesFound)
                    }
                }

            client.startDiscovery(
                serviceId,
                callback,
                discoveryOptions,
            )

            awaitClose { stopDiscovery() }
        }

    override fun stopDiscovery() {
        client.stopDiscovery()
    }

    override fun startAdvertising(name: String): Flow<Set<Device>> =
        callbackFlow {
            val devicesToConnect = mutableSetOf<Device>()

            val advertisingOptions =
                AdvertisingOptions
                    .Builder()
                    .setStrategy(Strategy.P2P_STAR)
                    .build()

            val connectionLifecycleCallback =
                createConnectionCallback(
                    onConnectionInitiated = { endpointId, connectionInfo ->
                        val device =
                            if (connectionInfo.isIncomingConnection) {
                                Device(
                                    id = endpointId,
                                    name = connectionInfo.endpointName,
                                    state = DeviceState.Connecting,
                                )
                            } else {
                                devicesToConnect.find { it.id == endpointId }!!
                            }

                        devicesToConnect.remove(device)
                        devicesToConnect.add(device.copy(state = DeviceState.Connecting))

                        trySend(devicesToConnect)
                    },
                    onConnectionResult = { endpointId, connectionResolution ->
                        when (connectionResolution.status.statusCode) {
                            ConnectionsStatusCodes.STATUS_OK -> {
                                devicesToConnect
                                    .find { it.id == endpointId }
                                    ?.copy(state = DeviceState.Connected)
                                    ?.let { devicesToConnect.add(it) }

                                trySend(devicesToConnect)
                            }

                            else -> {
                                devicesToConnect
                                    .find { it.id == endpointId }
                                    ?.copy(state = DeviceState.Error)
                                    ?.let { devicesToConnect.add(it) }

                                trySend(devicesToConnect)
                            }
                        }
                    },
                    onDisconnected = { endpointId ->
                        devicesToConnect
                            .find { it.id == endpointId }
                            ?.copy(state = DeviceState.Error)
                            ?.let { devicesToConnect.add(it) }

                        trySend(devicesToConnect)
                    },
                )

            client.startAdvertising(
                name,
                serviceId,
                connectionLifecycleCallback,
                advertisingOptions,
            )

            awaitClose { stopAdvertising() }
        }

    override fun stopAdvertising() {
        client.stopAdvertising()
    }

    override suspend fun requestConnection(
        from: String,
        to: Device,
    ): Flow<Result<DeviceState>> =
        callbackFlow {
            val callback =
                createConnectionCallback(
                    onConnectionInitiated = { endpointId, _ ->
                        client.acceptConnection(
                            endpointId,
                            Callback(),
                        )
                        trySend(Result.success(DeviceState.Connecting))
                    },
                    onConnectionResult = { _, connectionResolution ->
                        when (connectionResolution.status.statusCode) {
                            ConnectionsStatusCodes.STATUS_OK -> {
                                trySend(Result.success(DeviceState.Connected))
                            }

                            ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED -> {
                                trySend(Result.success(DeviceState.Error))
                            }

                            else -> {
                                trySend(Result.failure(Throwable(connectionResolution.status.statusMessage)))
                            }
                        }
                    },
                    onDisconnected = { _ ->
                        trySend(Result.success(DeviceState.Error))
                    },
                )

            val result =
                client.requestConnection(
                    from,
                    to.id,
                    callback,
                )

            result.addOnFailureListener { exception ->
                when {
                    exception is ApiException && exception.statusCode == ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> {
                        trySend(Result.success(DeviceState.Connected))
                    }

                    else -> {
                        trySend(Result.failure(exception))
                    }
                }
            }

            awaitClose { stopDiscovery() }
        }

    override suspend fun acceptConnection(device: Device): Result<DeviceState> =
        suspendCancellableCoroutine {
            val result = client.acceptConnection(device.id, Callback())

            result.addOnSuccessListener { _ ->
                if (it.isActive) {
                    it.resumeIfActive(Result.success(DeviceState.Connected))
                }
            }

            result.addOnFailureListener { exception ->
                if (it.isActive) {
                    it.resumeIfActive(Result.failure(exception))
                }
            }
        }

    override suspend fun rejectConnection(id: String): Result<DeviceState> =
        suspendCancellableCoroutine {
            val result = client.rejectConnection(id)

            result.addOnSuccessListener { _ ->
                if (it.isActive) {
                    it.resumeIfActive(Result.success(DeviceState.Error))
                }
            }

            result.addOnFailureListener { exception ->
                if (it.isActive) {
                    it.resumeIfActive(Result.failure(exception))
                }
            }
        }

    override fun disconnect(id: String) = client.disconnectFromEndpoint(id)

    override suspend fun cancelPayload(id: Long): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            client
                .cancelPayload(id)
                .addOnSuccessListener { continuation.resumeIfActive(Result.success(Unit)) }
                .addOnFailureListener { continuation.resumeIfActive(Result.failure(it)) }
        }

    override suspend fun sendPayload(
        ids: List<String>,
        payload: Any,
    ): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            client
                .sendPayload(ids, payload.toNearbyPayload())
                .addOnSuccessListener { continuation.resumeIfActive(Result.success(Unit)) }
                .addOnFailureListener { continuation.resumeIfActive(Result.failure(it)) }
        }

    private fun Any.toNearbyPayload(): NearbyApiPayload {
        if (this is gr.questweaver.model.File) {
            return uri
                .toUri()
                .openFileDescriptor(context)
                ?.let { pfd -> NearbyApiPayload.fromFile(pfd) }
                ?: error("Error opening file uri $uri")
        }

        return NearbyApiPayload.fromBytes(toNetworkDto().toByteArray())
    }

    private inline fun createConnectionCallback(
        crossinline onConnectionInitiated: (endpointId: String, connectionInfo: ConnectionInfo) -> Unit,
        crossinline onConnectionResult: (endpointId: String, connectionResolution: ConnectionResolution) -> Unit,
        crossinline onDisconnected: (endpointId: String) -> Unit,
    ) = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo,
        ) {
            onConnectionInitiated(endpointId, connectionInfo)
        }

        override fun onConnectionResult(
            endpointId: String,
            connectionResolution: ConnectionResolution,
        ) {
            onConnectionResult(endpointId, connectionResolution)
        }

        override fun onDisconnected(endpointId: String) {
            onDisconnected(endpointId)
        }
    }

    private fun <T> CancellableContinuation<T>.resumeIfActive(value: T) {
        if (isActive) {
            resume(value)
        }
    }

    private inner class Callback(
        scope: CoroutineScope = CoroutineScope(callbackJob),
        context: Context = this@NearbyConnectionsClientAndroidImpl.context,
        incomingPayloadsChannel: Channel<Payload> = this@NearbyConnectionsClientAndroidImpl.incomingPayloadsChannel,
    ) : PayloadCallback() {
        private val fileCallbackDelegate =
            FilePayloadCallbackDelegate(
                coroutineScope = scope,
                context = context,
                incomingPayloadsChannel = incomingPayloadsChannel,
            )
        private val streamCallbackDelegate =
            StreamPayloadCallbackDelegate(
                coroutineScope = scope,
                incomingPayloadsChannel = incomingPayloadsChannel,
            )
        private val messageCallbackDelegate =
            MessagePayloadCallbackDelegate(
                coroutineScope = scope,
                incomingPayloadsChannel = incomingPayloadsChannel,
            )

        override fun onPayloadReceived(
            from: String,
            payload: NearbyApiPayload,
        ) {
            when (payload.type) {
                NearbyApiPayloadType.BYTES -> {
                    if (fileCallbackDelegate.contains(payload.id)) {
                        payload
                            .asBytes()
                            ?.metadataOrNull()
                            ?.let { fileCallbackDelegate.updateMetadata(payload.id, it) }
                    } else {
                        messageCallbackDelegate.onPayloadReceived(payload)
                    }
                }

                NearbyApiPayloadType.STREAM -> streamCallbackDelegate.onPayloadReceived(payload)
                NearbyApiPayloadType.FILE -> fileCallbackDelegate.onPayloadReceived(payload)
            }
        }

        override fun onPayloadTransferUpdate(
            from: String,
            payload: PayloadTransferUpdate,
        ) {
            fileCallbackDelegate.onPayloadTransferUpdate(payload)

            when (payload.status) {
                PayloadTransferUpdate.Status.IN_PROGRESS -> {
                    outgoingPayloads.add(payload.payloadId)
                }

                PayloadTransferUpdate.Status.FAILURE,
                PayloadTransferUpdate.Status.CANCELED,
                PayloadTransferUpdate.Status.SUCCESS,
                -> {
                    outgoingPayloads.remove(payload.payloadId)
                }
            }
        }
    }
}
