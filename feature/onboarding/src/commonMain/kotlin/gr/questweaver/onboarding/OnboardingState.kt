package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

data class OnboardingState(
    val isRegistered: Boolean = false,
    val backStack: List<Route> = listOf(OnboardingRoute.Welcome)
)
