package gr.questweaver.android

import android.app.Application
import gr.questweaver.common.coroutines.coroutinesModule
import gr.questweaver.data.device.deviceModule
import gr.questweaver.data.game.gameModule
import gr.questweaver.data.user.userModule
import gr.questweaver.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MainApp)
            modules(
                coroutinesModule,
                networkModule,
                deviceModule,
                userModule,
                gameModule,
            )
        }
    }
}
