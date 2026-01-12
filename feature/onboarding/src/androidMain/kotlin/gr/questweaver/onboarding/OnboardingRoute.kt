package gr.questweaver.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.screens.RegistrationScreen
import gr.questweaver.onboarding.screens.TutorialScreen
import gr.questweaver.onboarding.screens.WelcomeScreen

@Composable
fun OnboardingRoute(
    route: OnboardingRoute,
    onNavigate: (Route) -> Unit,
) {
    val viewModel = viewModel { OnboardingViewModel() }

    val progress by
    animateFloatAsState(
        targetValue =
            when (route) {
                OnboardingRoute.Welcome -> 0.33f
                OnboardingRoute.Registration -> 0.66f
                OnboardingRoute.Tutorial -> 1.0f
            },
        label = "onboarding_progress"
    )

    Scaffold(
        topBar = {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().safeDrawingPadding(),
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (route) {
                is OnboardingRoute.Welcome ->
                    WelcomeScreen(
                        onStartClick = {
                            onNavigate(OnboardingRoute.Registration)
                        }
                    )

                is OnboardingRoute.Registration ->
                    RegistrationScreen(
                        onRegisterClick = { name ->
                            viewModel.registerUser(name)
                            onNavigate(OnboardingRoute.Tutorial)
                        }
                    )

                is OnboardingRoute.Tutorial -> TutorialScreen(onCompleteClick = {})
            }
        }
    }
}
