package gr.questweaver.di

import gr.questweaver.di.common.dispatchersModule
import gr.questweaver.di.domain.userUseCasesModule
import org.koin.core.KoinApplication

fun KoinApplication.injectModules() {
    // Common modules
    modules(dispatchersModule)

    // Domain modules
    modules(userUseCasesModule)
}
