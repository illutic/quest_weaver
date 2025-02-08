package gr.questweaver.local.user

import org.koin.core.definition.KoinDefinition
import org.koin.core.module.Module

internal actual fun Module.provideUserLocalDataSource(): KoinDefinition<UserLocalDataSource> =
    single {
        TODO()
    }
