package gr.questweaver.di.common

import gr.questweaver.core.common.QuestWeaverDispatchers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

internal val dispatchersModule =
    module {
        single { Dispatchers.IO } withOptions {
            named<QuestWeaverDispatchers.IO>()
            createdAtStart()
        }

        single { Dispatchers.Default } withOptions {
            named<QuestWeaverDispatchers.Default>()
            createdAtStart()
        }

        single { Dispatchers.Main } withOptions {
            named<QuestWeaverDispatchers.Main>()
            createdAtStart()
        }
    }
