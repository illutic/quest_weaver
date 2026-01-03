package gr.questweaver.onboarding

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun OnboardingScreen(
    state: OnboardingState,
    modifier: Modifier = Modifier,
) {
    Text("User is registered: ${state.isRegistered}", modifier = modifier)
}