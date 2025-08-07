package gr.questweaver.core.database

import gr.questweaver.core.common.QuestWeaverDispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal actual val appDatabaseModule: Module = module {
    single<AppDatabase> {
        getRoomDatabase(
            builder = getDatabaseBuilder(androidApplication()),
            coroutineDispatcher = get(named<QuestWeaverDispatchers.IO>())
        )
    }
}