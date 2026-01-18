package gr.questweaver.onboarding

data class OnboardingState(
    val name: String = "",
    val strings: OnboardingStrings = OnboardingStrings.Default,
    val error: String? = null,
) {
    companion object {
        val Default = OnboardingState()
    }
}
