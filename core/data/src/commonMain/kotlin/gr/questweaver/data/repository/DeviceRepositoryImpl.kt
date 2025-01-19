package gr.questweaver.data.repository

import gr.questweaver.common.coroutines.provideDefaultDispatcher
import gr.questweaver.domain.repository.DeviceRepository
import gr.questweaver.model.Device
import gr.questweaver.network.NearbyConnectionsClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.koin.dsl.module

val deviceModule =
    module {
        single<DeviceRepository> { DeviceRepositoryImpl(get(), provideDefaultDispatcher()) }
    }

class DeviceRepositoryImpl(
    private val nearbyClient: NearbyConnectionsClient,
    private val dispatcher: CoroutineDispatcher,
) : DeviceRepository {
    private val _devices = MutableStateFlow<Set<Device>>(emptySet())
    override val devices: StateFlow<Set<Device>> = _devices

    override suspend fun discover() =
        withDispatcherScope {
            nearbyClient.stopDiscovery()
            _devices.value = emptySet()

            nearbyClient
                .startDiscovery()
                .collectLatest {
                    _devices.value += it
                }
        }

    override fun stopDiscovery() = nearbyClient.stopDiscovery()

    override suspend fun advertise(name: String) =
        withDispatcherScope {
            nearbyClient.stopAdvertising()
            _devices.value = emptySet()

            nearbyClient
                .startAdvertising(name)
                .collectLatest {
                    _devices.value += it
                }
        }

    override fun stopAdvertising() = nearbyClient.stopAdvertising()

    override suspend fun requestConnection(
        id: String,
        name: String,
    ) = withDispatcherScope {
        val device = getDeviceById(id)
        nearbyClient
            .requestConnection(name, device)
            .collectLatest {
                _devices.value += device.copy(state = it.getOrThrow())
            }
    }

    override suspend fun acceptConnection(id: String) =
        withDispatcherScope {
            val deviceToAccept = getDeviceById(id)
            val state = nearbyClient.acceptConnection(deviceToAccept).getOrThrow()
            _devices.value += deviceToAccept.copy(state = state)
        }

    override suspend fun rejectConnection(id: String) =
        withDispatcherScope {
            val deviceToReject = getDeviceById(id)
            val state = nearbyClient.rejectConnection(deviceToReject).getOrThrow()
            _devices.value += deviceToReject.copy(state = state)
        }

    override fun disconnect(id: String) {
        nearbyClient.disconnect(id)
        _devices.value -= getDeviceById(id)
    }

    private fun getDeviceById(id: String): Device = _devices.value.find { it.id == id } ?: error("Device with $id not found")

    private suspend inline fun withDispatcherScope(crossinline block: suspend CoroutineScope.() -> Unit) {
        supervisorScope {
            withContext(dispatcher) {
                block()
            }
        }
    }
}
