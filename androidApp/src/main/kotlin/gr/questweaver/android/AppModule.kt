package gr.questweaver.android

import android.content.Context
import android.content.pm.ApplicationInfo
import gr.questweaver.core.common.dispatchersModule
import gr.questweaver.core.database.databaseModule
import gr.questweaver.user.data.userDataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level

fun KoinApplication.injectModules() {
    // Common modules
    modules(dispatchersModule, databaseModule)

    // Feature modules
    modules(userDataModule)
}

fun KoinApplication.setupAndroid(context: Context) {
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    androidLogger(if (isDebug) Level.DEBUG else Level.NONE)
    androidContext(context)
}