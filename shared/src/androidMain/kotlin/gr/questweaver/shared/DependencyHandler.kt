package gr.questweaver.shared

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin(application: Application) {
    startKoin {
        setupAndroid(application)
        modules(appModules())
    }
}

private fun KoinApplication.setupAndroid(context: Context) {
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    androidLogger(if (isDebug) Level.DEBUG else Level.NONE)
    androidContext(context)
}