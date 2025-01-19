package gr.questweaver.domain.repository

import gr.questweaver.model.Device
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    // Discoverer
    fun discover(): Flow<Set<Device>>

    fun stopDiscovery()

    // Controller
    fun advertise(name: String): Flow<Set<Device>>

    fun stopAdvertising()

    // Connection
    fun requestConnection(
        id: String,
        name: String,
    ): Flow<Device>

    fun acceptConnection(id: String): Flow<Device>

    fun rejectConnection(id: String): Flow<Device>

    // Disconnection
    fun disconnect(id: String): Flow<Device>

    suspend fun getDeviceState(id: String): Result<Device>
}
