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
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.screens.RegistrationScreen
import gr.questweaver.onboarding.screens.TutorialScreen
import gr.questweaver.onboarding.screens.WelcomeScreen

@Composable
fun OnboardingUiRoute(onNavigate: (Route) -> Unit) {
    val viewModel = viewModel { OnboardingViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentRoute = state.backStack.last()

    val progress by
        animateFloatAsState(
            targetValue =
                when (currentRoute) {
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
            NavDisplay(
                backStack = state.backStack,
                onBack = { viewModel.navigateBack() },
                entryProvider = { route ->
                    NavEntry(route) {
                        when (route) {
                            is OnboardingRoute.Welcome -> {
                                WelcomeScreen(
                                    strings = state.strings,
                                    drawables = state.drawables,
                                    onStartClick = {
                                        viewModel.navigateTo(
                                            OnboardingRoute.Registration,
                                        )
                                    },
                                )
                            }

                            is OnboardingRoute.Registration -> {
                                RegistrationScreen(
                                    strings = state.strings,
                                    name = state.name,
                                    onNameChange = viewModel::onNameChange,
                                    onRegisterClick = { name ->
                                        viewModel.registerUser(
                                            name,
                                        )
                                        viewModel.navigateTo(
                                            OnboardingRoute.Tutorial,
                                        )
                                    },
                                    onRandomNameClick = viewModel::generateRandomName,
                                    error = state.error,
                                    onErrorDismiss = viewModel::clearError,
                                )
                            }

                            is OnboardingRoute.Tutorial -> {
                                TutorialScreen(
                                    strings = state.strings,
                                    onCompleteClick = {
                                        onNavigate(
                                            OnboardingRoute.Graph,
                                        ) // Exit onboarding
                                    },
                                )
                            }

                            else -> {
                                error("Unknown route: $route")
                            }
                        }
                    }
                },
            )
        }
    }
}
