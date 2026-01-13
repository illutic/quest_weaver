package gr.questweaver.core.database

import gr.questweaver.core.common.QuestWeaverDispatchers
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

actual val databaseModule: Module =
    module {
        single<AppDatabase> {
            getRoomDatabase(
                builder = getDatabaseBuilder(),
                coroutineDispatcher = get(named(QuestWeaverDispatchers.Io)),
            )
        }
    }
