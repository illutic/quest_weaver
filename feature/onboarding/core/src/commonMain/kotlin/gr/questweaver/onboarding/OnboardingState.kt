package gr.questweaver.onboarding

import gr.questweaver.navigation.NavigationState

data class OnboardingState(
    val name: String = "",
    val strings: OnboardingStrings = OnboardingStrings.Default,
    val error: String? = null,
    val navigationState: NavigationState = NavigationState()
) {
    companion object {
        val Default = OnboardingState()
    }
}
