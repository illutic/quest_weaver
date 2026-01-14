package gr.questweaver.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeUiRoute
import gr.questweaver.navigation.NavigationState
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.onboarding.OnboardingUiRoute

@Composable
fun MainNavigation(
        navigationState: NavigationState,
        onBack: () -> Unit,
        onNavigate: (Route) -> Unit,
) {
    NavDisplay(
            backStack = navigationState.backStack,
            onBack = onBack,
            entryProvider = { key ->
                when (key) {
                    is OnboardingRoute.Graph -> {
                        NavEntry(key) { OnboardingUiRoute(onNavigate = onNavigate) }
                    }
                    is HomeRoute.Graph -> {
                        NavEntry(key) { HomeUiRoute(onNavigate = onNavigate) }
                    }
                    else -> {
                        error("Unknown route: $key")
                    }
                }
            },
    )
}
