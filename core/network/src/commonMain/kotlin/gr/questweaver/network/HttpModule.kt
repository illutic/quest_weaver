package gr.questweaver.network

import gr.questweaver.coroutines.provideIoDispatcher
import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module
import org.koin.dsl.module

val httpModule =
    module {
        provideRemoteDataSourceSingleton()
    }

private fun Module.provideRemoteDataSourceSingleton(): KoinDefinition<HttpDataSource> =
    single {
        HttpDataSourceImpl(
            httpClient = getHttpClient(),
            ioDispatcher = provideIoDispatcher(),
        )
    }
