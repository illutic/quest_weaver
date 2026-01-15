package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

data class OnboardingState(
    val isRegistered: Boolean = false,
    val backStack: List<Route> = listOf(OnboardingRoute.Welcome),
    val name: String = "",
    val strings: OnboardingStrings = OnboardingStrings.Default,
    val drawables: OnboardingDrawables = OnboardingDrawables.Default,
    val error: String? = null,
) {
    companion object {
        val Default = OnboardingState()
    }
}
