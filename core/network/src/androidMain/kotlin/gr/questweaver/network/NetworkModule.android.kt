package gr.questweaver.network

import gr.questweaver.coroutines.provideIoDispatcher
import gr.questweaver.network.nearby.NearbyConnectionsClientAndroidImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

internal actual fun Module.provideNearbyConnectionsClient(): KoinDefinition<NearbyConnectionsClient> =
    single<NearbyConnectionsClient> {
        NearbyConnectionsClientAndroidImpl(
            serviceId = SERVICE_ID,
            ioDispatcher = provideIoDispatcher(),
            context = androidApplication(),
        )
    }
