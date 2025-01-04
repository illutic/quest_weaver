package gr.questweaver.network

import gr.questweaver.model.Device
import gr.questweaver.model.DeviceState
import kotlinx.coroutines.flow.Flow

interface NearbyConnectionsClient {
    fun startDiscovery(): Flow<Set<Device>>

    fun stopDiscovery()

    fun startAdvertising(name: String): Flow<Set<Device>>

    fun stopAdvertising()

    suspend fun requestConnection(
        from: Device,
        to: Device,
    ): Result<DeviceState>

    suspend fun acceptConnection(id: String): Result<DeviceState>

    suspend fun rejectConnection(id: String): Result<DeviceState>

    fun disconnect(id: String)
}
