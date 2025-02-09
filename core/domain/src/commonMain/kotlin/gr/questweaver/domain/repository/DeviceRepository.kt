package gr.questweaver.domain.repository

import gr.questweaver.model.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<Set<Device>>

    // Discoverer
    suspend fun discover(): Result<Unit>

    fun stopDiscovery()

    // Controller
    suspend fun advertise(name: String): Result<Unit>

    fun stopAdvertising()

    // Connection
    suspend fun requestConnection(
        id: String,
        name: String,
    ): Result<Unit>

    suspend fun acceptConnection(id: String): Result<Unit>

    suspend fun rejectConnection(id: String): Result<Unit>

    // Disconnection
    fun disconnect(id: String)
}
