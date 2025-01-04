package gr.questweaver.network

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

val networkModule: Module =
    module {
        provideNearbyConnectionsClient()
    }

expect fun Module.provideNearbyConnectionsClient(): KoinDefinition<NearbyConnectionsClient>

internal const val SERVICE_ID = "gr.questweaver"
