package gr.questweaver.shared

import gr.questweaver.core.common.dispatchersModule
import gr.questweaver.core.database.databaseModule
import gr.questweaver.user.data.userDataModule
import gr.questweaver.user.data.userUseCasesModule

fun appModules() = listOf(
    // Common modules
    dispatchersModule,
    databaseModule,

    // Feature modules
    userDataModule,
    userUseCasesModule
)