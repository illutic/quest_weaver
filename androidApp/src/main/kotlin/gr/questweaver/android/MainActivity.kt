package gr.questweaver.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import gr.questweaver.android.navigation.MainNavigation
import gr.questweaver.core.ui.QuestWeaverTheme
import gr.questweaver.shared.NavigationViewModel
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val viewModel: NavigationViewModel by viewModels {
        val isUserRegisteredUseCase: IsUserRegisteredUseCase by inject()
        NavigationViewModel.createFactory(isUserRegisteredUseCase = isUserRegisteredUseCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setKeepOnScreenCondition { viewModel.navigationState.value.isLoading }

        setContent {
            val navigationState by viewModel.navigationState.collectAsStateWithLifecycle()
            if (navigationState.isLoading) return@setContent

            QuestWeaverTheme {
                MainNavigation(
                    navigationState = navigationState,
                    onBack = { viewModel.navigateBack() },
                    onNavigate = { viewModel.navigateTo(it) },
                )
            }
        }
    }
}
