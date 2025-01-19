package gr.questweaver.domain.repository

import gr.questweaver.model.Device
import kotlinx.coroutines.flow.StateFlow

interface DeviceRepository {
    val devices: StateFlow<Set<Device>>

    // Discoverer
    suspend fun discover()

    fun stopDiscovery()

    // Controller
    suspend fun advertise(name: String)

    fun stopAdvertising()

    // Connection
    suspend fun requestConnection(
        id: String,
        name: String,
    )

    suspend fun acceptConnection(id: String)

    suspend fun rejectConnection(id: String)

    // Disconnection
    fun disconnect(id: String)
}
