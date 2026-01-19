package gr.questweaver.android.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.ai.AiRoute
import gr.questweaver.ai.screens.AiAssistantScreen
import gr.questweaver.bottombar.BottomBar
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.screens.HomeSheetUi
import gr.questweaver.home.screens.HomeUiRoute
import gr.questweaver.navigation.NavigationState
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.onboarding.OnboardingUiRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigation(
        navigationState: NavigationState,
        onBack: () -> Unit,
) {
    Scaffold(bottomBar = { BottomBar() }) { padding ->
        NavDisplay(
                backStack = navigationState.visibleBackStack,
                onBack = onBack,
                entryProvider = { key ->
                    when (key) {
                        is OnboardingRoute -> {
                            NavEntry(key) { OnboardingUiRoute(route = key) }
                        }
                        is HomeRoute -> {
                            NavEntry(key) { HomeUiRoute(route = key) }
                        }
                        is AiRoute.AiAssistant -> {
                            NavEntry(key) { AiAssistantScreen() }
                        }
                        is gr.questweaver.search.SearchRoute.Search -> {
                            NavEntry(key) { gr.questweaver.search.screens.SearchScreen() }
                        }
                        is gr.questweaver.settings.SettingsRoute.Settings -> {
                            NavEntry(key) { gr.questweaver.settings.SettingsScreen() }
                        }
                        else -> {
                            NavEntry(key) { Text("Unknown route: $key") }
                        }
                    }
                },
        )
    }

    if (navigationState.sheetBackStack.isNotEmpty()) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        val currentSheet = navigationState.sheetBackStack.last()

        ModalBottomSheet(
                onDismissRequest = onBack,
                sheetState = sheetState,
                dragHandle = null,
        ) {
            when (currentSheet) {
                is HomeRoute -> {
                    HomeSheetUi(
                            route = currentSheet,
                            onBack = onBack,
                    )
                }
                else -> {
                    Text("Unknown sheet route: $currentSheet")
                }
            }
        }
    }
}
