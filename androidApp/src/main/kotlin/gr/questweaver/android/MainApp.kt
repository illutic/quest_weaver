package gr.questweaver.android

import android.app.Application
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            setupAndroid(this@MainApp)
            injectModules()
        }
    }
}
