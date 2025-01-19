package gr.questweaver.network

import gr.questweaver.model.Device
import gr.questweaver.model.DeviceState
import kotlinx.coroutines.flow.Flow

interface NearbyConnectionsClient {
    val incomingPayloads: Flow<Any>

    fun startDiscovery(): Flow<Set<Device>>

    fun stopDiscovery()

    fun startAdvertising(name: String): Flow<Set<Device>>

    fun stopAdvertising()

    fun disconnect(id: String)

    suspend fun requestConnection(
        from: String,
        to: Device,
    ): Flow<Result<DeviceState>>

    suspend fun acceptConnection(device: Device): Result<DeviceState>

    suspend fun rejectConnection(device: Device): Result<DeviceState>

    suspend fun cancelPayload(id: Long): Result<Unit>

    suspend fun sendPayload(
        ids: List<String>,
        payload: Any,
    ): Result<Unit>
}
