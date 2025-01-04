package gr.questweaver.network

import gr.questweaver.coroutines.provideIoDispatcher
import org.koin.android.ext.koin.androidApplication
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

actual fun Module.provideNearbyConnectionsClient(): KoinDefinition<NearbyConnectionsClient> =
    single<NearbyConnectionsClient> {
        NearbyConnectionsClientAndroidImpl(
            serviceId = SERVICE_ID,
            payloadCallback = get(),
            ioDispatcher = provideIoDispatcher(),
            context = androidApplication(),
        )
    }
