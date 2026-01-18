package gr.questweaver.shared

import gr.questweaver.bottombar.bottomBarModule
import gr.questweaver.core.common.dispatchersModule
import gr.questweaver.core.database.coreDatabaseModule
import gr.questweaver.core.database.databaseModule
import gr.questweaver.navigation.navigationModule
import gr.questweaver.user.data.userDataModule
import gr.questweaver.user.data.userUseCasesModule

fun appModules() =
    listOf(
        // Common modules
        dispatchersModule,
        databaseModule,
        coreDatabaseModule,
        // Feature modules
        userDataModule,
        userUseCasesModule,
        bottomBarModule,
        navigationModule,
    )
