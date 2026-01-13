package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

data class OnboardingState(
    val isRegistered: Boolean = false,
    val backStack: List<Route> = listOf(OnboardingRoute.Welcome),
    val name: String = "",
    val strings: OnboardingStrings = OnboardingStrings.Empty,
    val drawables: OnboardingDrawables = OnboardingDrawables.Empty,
    val error: String? = null,
)

val Empty: OnboardingState = OnboardingState()
