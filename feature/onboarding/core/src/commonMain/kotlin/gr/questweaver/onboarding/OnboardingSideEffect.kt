package gr.questweaver.onboarding

import gr.questweaver.navigation.Route

sealed interface OnboardingSideEffect {
    data class Navigate(val route: Route) : OnboardingSideEffect
    data class ShowToast(val message: String) : OnboardingSideEffect
}
