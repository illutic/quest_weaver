package gr.questweaver.network

import gr.questweaver.model.Device
import gr.questweaver.model.DeviceState
import gr.questweaver.network.model.Payload
import kotlinx.coroutines.flow.Flow

interface NearbyConnectionsClient {
    val incomingPayloads: Flow<Payload>
    val outgoingPayloads: Set<Long>

    fun startDiscovery(): Flow<Set<Device>>

    fun stopDiscovery()

    fun startAdvertising(name: String): Flow<Set<Device>>

    fun stopAdvertising()

    fun disconnect(id: String)

    suspend fun requestConnection(
        from: Device,
        to: Device,
    ): Result<DeviceState>

    suspend fun acceptConnection(device: Device): Result<DeviceState>

    suspend fun rejectConnection(id: String): Result<DeviceState>

    suspend fun cancelPayload(id: Long): Result<Unit>

    suspend fun sendPayload(
        ids: List<String>,
        payload: Payload,
    ): Result<Unit>
}
