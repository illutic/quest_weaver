package gr.questweaver.android

import android.content.Context
import android.content.pm.ApplicationInfo
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level

fun KoinApplication.setupAndroid(context: Context) {
    val isDebug = context.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0

    androidLogger(if (isDebug) Level.DEBUG else Level.NONE)
    androidContext(context)
}
