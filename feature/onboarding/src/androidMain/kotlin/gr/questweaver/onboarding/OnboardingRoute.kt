package gr.questweaver.onboarding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun OnboardingRoute() {
    val viewModel = viewModel { OnboardingViewModel() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    OnboardingScreen(state = state)
}