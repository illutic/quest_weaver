package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

data class OnboardingState(
    val backStack: List<Route> = listOf(OnboardingRoute.Welcome),
    val name: String = "",
    val strings: OnboardingStrings = OnboardingStrings.Default,
    val error: String? = null,
) {
    companion object {
        val Default = OnboardingState()
    }
}
