package gr.questweaver.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.qualifier.named
import org.koin.core.scope.Scope
import org.koin.dsl.module

private const val IO_DISPATCHER = "ioDispatcher"
private const val MAIN_DISPATCHER = "mainDispatcher"
private const val DEFAULT_DISPATCHER = "defaultDispatcher"

fun Scope.provideIoDispatcher(): CoroutineDispatcher = get(named(IO_DISPATCHER))

fun Scope.provideMainDispatcher(): CoroutineDispatcher = get(named(MAIN_DISPATCHER))

fun Scope.provideDefaultDispatcher(): CoroutineDispatcher = get(named(DEFAULT_DISPATCHER))

val coroutinesModule =
    module {
        single(named(IO_DISPATCHER)) {
            Dispatchers.IO
        }

        single(named(MAIN_DISPATCHER)) {
            Dispatchers.Main
        }

        single(named(DEFAULT_DISPATCHER)) {
            Dispatchers.Default
        }
    }
