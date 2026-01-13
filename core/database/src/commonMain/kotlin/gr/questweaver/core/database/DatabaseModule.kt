package gr.questweaver.core.database

import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val coreDatabaseModule =
    module {
        single { get<AppDatabase>().userDao() }
    }
