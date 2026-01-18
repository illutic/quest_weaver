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
import gr.questweaver.onboarding.screens.RegistrationScreen
import gr.questweaver.onboarding.screens.TutorialScreen
import gr.questweaver.onboarding.screens.WelcomeScreen

@Composable
fun OnboardingUiRoute(route: OnboardingRoute) {
    val viewModel = viewModel { OnboardingViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val progress by
    animateFloatAsState(
        targetValue =
            when (route) {
                OnboardingRoute.Welcome -> 0.33f
                OnboardingRoute.Registration -> 0.66f
                OnboardingRoute.Tutorial -> 1.0f
                else -> 0f
            },
        label = "onboarding_progress",
    )

    Scaffold(
        topBar = {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().safeDrawingPadding(),
            )
        },
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (route) {
                is OnboardingRoute.Welcome -> {
                    WelcomeScreen(
                        strings = state.strings,
                        onStartClick = {
                            viewModel.onEvent(
                                OnboardingEvent.OnNavigate(OnboardingRoute.Registration),
                            )
                        },
                    )
                }

                is OnboardingRoute.Registration -> {
                    RegistrationScreen(
                        strings = state.strings,
                        name = state.name,
                        onNameChange = { viewModel.onEvent(OnboardingEvent.OnNameChange(it)) },
                        onRegisterClick = { name ->
                            viewModel.onEvent(OnboardingEvent.OnRegisterClick(name))
                            viewModel.onEvent(
                                OnboardingEvent.OnNavigate(OnboardingRoute.Tutorial),
                            )
                        },
                        onRandomNameClick = {
                            viewModel.onEvent(OnboardingEvent.OnGenerateRandomName)
                        },
                        error = state.error,
                        onErrorDismiss = { viewModel.onEvent(OnboardingEvent.OnClearError) },
                    )
                }

                is OnboardingRoute.Tutorial -> {
                    TutorialScreen(
                        strings = state.strings,
                        onCompleteClick = {
                            viewModel.onEvent(OnboardingEvent.OnCompleteOnboarding)
                        }
                    )
                }

                else -> {
                    // Fallback or empty
                }
            }
        }
    }
}
