package gr.questweaver.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import gr.questweaver.core.ui.QuestWeaverTheme
import gr.questweaver.shared.NavigationViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: NavigationViewModel by viewModels {
        NavigationViewModel.createFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.navigationState.value.isLoading }

        setContent {
            QuestWeaverTheme {
                MainNavigationRoute()
            }
        }
    }
}
