package gr.questweaver.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dispatchersModule =
    module(createdAtStart = true) {
        single(named(QuestWeaverDispatchers.Io)) { ioDispatcher }

        single(named(QuestWeaverDispatchers.Default)) { Dispatchers.Default }

        single(named(QuestWeaverDispatchers.Main)) { Dispatchers.Main }
    }

internal expect val ioDispatcher: CoroutineDispatcher