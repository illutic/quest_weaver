package gr.questweaver.core.database

import org.koin.core.module.Module
import org.koin.dsl.module

internal expect val appDatabaseModule: Module

val databaseModule = module {
    includes(appDatabaseModule)
}