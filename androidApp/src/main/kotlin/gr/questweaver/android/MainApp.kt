package gr.questweaver.android

import android.app.Application
import gr.questweaver.shared.initKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}
