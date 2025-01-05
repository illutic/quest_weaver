package gr.questweaver.network.nearby

import android.content.Context
import androidx.core.net.toUri
import com.google.android.gms.common.api.ApiException
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
import gr.questweaver.network.StreamAndroidImpl
import gr.questweaver.network.model.File
import gr.questweaver.network.model.Message
import gr.questweaver.network.model.Payload
import gr.questweaver.network.model.metadataOrNull
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

private typealias NearbyApiPayload = com.google.android.gms.nearby.connection.Payload
private typealias NearbyApiPayloadType = com.google.android.gms.nearby.connection.Payload.Type

class NearbyConnectionsClientAndroidImpl(
    ioDispatcher: CoroutineDispatcher,
    private val context: Context,
    private val serviceId: String,
) : NearbyConnectionsClient {
    override val outgoingPayloads = mutableSetOf<Long>()
    private val incomingPayloadsChannel = Channel<Payload>()
    private val client: ConnectionsClient = Nearby.getConnectionsClient(context)
    private val discoveryJob = SupervisorJob() + ioDispatcher + CoroutineName("Discovery")
    private val advertisingJob = SupervisorJob() + ioDispatcher + CoroutineName("Advertising")
    private val callbackJob = SupervisorJob() + ioDispatcher + CoroutineName("Callback")

    override val incomingPayloads: Flow<Payload> = incomingPayloadsChannel.receiveAsFlow()

    override fun startDiscovery(): Flow<Set<Device>> =
        callbackFlow {
            val devicesFound = mutableSetOf<Device>()
            val discoveryOptions =
                DiscoveryOptions
                    .Builder()
                    .setStrategy(Strategy.P2P_POINT_TO_POINT)
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

                        emit(devicesFound)
                    }

                    override fun onEndpointLost(endpointId: String) {
                        devicesFound.removeIf { it.id == endpointId }
                        emit(devicesFound)
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
        discoveryJob.cancelChildren()
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
                    coroutineScope = CoroutineScope(advertisingJob),
                    onConnectionInitiated = { endpointId, connectionInfo ->
                        devicesToConnect
                            .find { it.id == endpointId }
                            ?.copy(
                                name = connectionInfo.endpointName,
                                state = DeviceState.Connecting,
                            )?.let { devicesToConnect.add(it) }

                        emit(devicesToConnect)
                    },
                    onConnectionResult = { endpointId, connectionResolution ->
                        when (connectionResolution.status.statusCode) {
                            ConnectionsStatusCodes.STATUS_OK -> {
                                devicesToConnect
                                    .find { it.id == endpointId }
                                    ?.copy(state = DeviceState.Connected)
                                    ?.let { devicesToConnect.add(it) }

                                emit(devicesToConnect)
                            }

                            else -> {
                                devicesToConnect
                                    .find { it.id == endpointId }
                                    ?.copy(state = DeviceState.Error)
                                    ?.let { devicesToConnect.add(it) }

                                emit(devicesToConnect)
                            }
                        }
                    },
                    onDisconnected = { endpointId ->
                        devicesToConnect
                            .find { it.id == endpointId }
                            ?.copy(state = DeviceState.Error)
                            ?.let { devicesToConnect.add(it) }

                        emit(devicesToConnect)
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
        advertisingJob.cancelChildren()
    }

    override suspend fun requestConnection(
        from: Device,
        to: Device,
    ): Result<DeviceState> =
        suspendCancellableCoroutine {
            val callback =
                createConnectionCallback(
                    coroutineScope = CoroutineScope(discoveryJob),
                    onConnectionInitiated = { endpointId, _ ->
                        client.acceptConnection(
                            endpointId,
                            Callback(),
                        )
                        it.resume(Result.success(DeviceState.Connecting))
                    },
                    onConnectionResult = { _, connectionResolution ->
                        when (connectionResolution.status.statusCode) {
                            ConnectionsStatusCodes.STATUS_OK -> {
                                it.resume(Result.success(DeviceState.Connected))
                            }

                            else -> {
                                it.resume(Result.failure(Throwable(connectionResolution.status.statusMessage)))
                            }
                        }
                    },
                    onDisconnected = { _ ->
                        it.resume(Result.success(DeviceState.Error))
                    },
                )

            val result =
                client.requestConnection(
                    from.name,
                    to.id,
                    callback,
                )

            result.addOnFailureListener { exception ->
                when {
                    exception is ApiException && exception.statusCode == ConnectionsStatusCodes.STATUS_ALREADY_CONNECTED_TO_ENDPOINT -> {
                        it.resume(Result.success(DeviceState.Connected))
                    }

                    else -> {
                        it.resume(Result.failure(exception))
                    }
                }
            }

            it.invokeOnCancellation {
                stopDiscovery()
            }
        }

    override suspend fun acceptConnection(device: Device): Result<DeviceState> =
        suspendCancellableCoroutine {
            val result = client.acceptConnection(device.id, Callback())

            result.addOnSuccessListener { _ ->
                it.resume(Result.success(DeviceState.Connected))
            }

            result.addOnFailureListener { exception ->
                it.resume(Result.failure(exception))
            }
        }

    override suspend fun rejectConnection(id: String): Result<DeviceState> =
        suspendCancellableCoroutine {
            val result = client.rejectConnection(id)

            result.addOnSuccessListener { _ ->
                it.resume(Result.success(DeviceState.Error))
            }

            result.addOnFailureListener { exception ->
                it.resume(Result.failure(exception))
            }
        }

    override fun disconnect(id: String) = client.disconnectFromEndpoint(id)

    override suspend fun cancelPayload(id: Long): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            client
                .cancelPayload(id)
                .addOnSuccessListener { continuation.resume(Result.success(Unit)) }
                .addOnFailureListener { continuation.resume(Result.failure(it)) }
        }

    override suspend fun sendPayload(
        ids: List<String>,
        payload: Payload,
    ): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            client
                .sendPayload(ids, payload.toNearbyPayload())
                .addOnSuccessListener { continuation.resume(Result.success(Unit)) }
                .addOnFailureListener { continuation.resume(Result.failure(it)) }
        }

    private fun Payload.toNearbyPayload() =
        when (this) {
            is Message -> NearbyApiPayload.fromBytes(value)
            is File ->
                uri
                    .toUri()
                    .openFileDescriptor(context)
                    ?.let { pfd -> NearbyApiPayload.fromFile(pfd) }
                    ?: error("Error opening file uri $uri")

            is StreamAndroidImpl -> NearbyApiPayload.fromStream(value)
            else -> error("Unsupported payload type $this")
        }

    private fun <T> ProducerScope<T>.emit(data: T) {
        launch(discoveryJob) { send(data) }
    }

    private inline fun createConnectionCallback(
        coroutineScope: CoroutineScope,
        crossinline onConnectionInitiated: suspend (endpointId: String, connectionInfo: ConnectionInfo) -> Unit,
        crossinline onConnectionResult: suspend (endpointId: String, connectionResolution: ConnectionResolution) -> Unit,
        crossinline onDisconnected: suspend (endpointId: String) -> Unit,
    ) = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(
            endpointId: String,
            connectionInfo: ConnectionInfo,
        ) {
            coroutineScope.launch { onConnectionInitiated(endpointId, connectionInfo) }
        }

        override fun onConnectionResult(
            endpointId: String,
            connectionResolution: ConnectionResolution,
        ) {
            coroutineScope.launch { onConnectionResult(endpointId, connectionResolution) }
        }

        override fun onDisconnected(endpointId: String) {
            coroutineScope.launch { onDisconnected(endpointId) }
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
