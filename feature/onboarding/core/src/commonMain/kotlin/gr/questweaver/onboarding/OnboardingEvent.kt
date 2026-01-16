package gr.questweaver.onboarding

sealed interface OnboardingEvent {
    data class OnNameChange(val name: String) : OnboardingEvent
    data object OnGenerateRandomName : OnboardingEvent
    data class OnRegisterClick(val name: String) : OnboardingEvent
    data object OnCompleteOnboarding : OnboardingEvent
    data class OnNavigate(val route: OnboardingRoute) : OnboardingEvent
    data object OnNavigateBack : OnboardingEvent
    data object OnClearError : OnboardingEvent
}
