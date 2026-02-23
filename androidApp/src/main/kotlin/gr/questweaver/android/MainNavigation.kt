package gr.questweaver.android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.ai.AiRoute
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.navigation.HomeRoot
import gr.questweaver.home.screens.HomeSheetUi
import gr.questweaver.navigation.AppNavDisplay
import gr.questweaver.navigation.NavigationState
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.onboarding.OnboardingUiRoute
import gr.questweaver.search.SearchRoute
import gr.questweaver.settings.SettingsRoute
import gr.questweaver.shared.NavigationViewModel

@Composable
fun MainNavigationRoute() {
    val viewModel = viewModel<NavigationViewModel>()
    val navState by viewModel.navigationState.collectAsStateWithLifecycle()
    val sheetNavState by viewModel.sheetState.collectAsStateWithLifecycle()

    MainNavigation(
        navState = navState,
        sheetNavState = sheetNavState,
        onBack = { viewModel.navigateBack() },
        onSheetBack = { viewModel.dismissSheet() },
        onDismissSheet = { viewModel.dismissSheet() },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainNavigation(
    navState: NavigationState,
    sheetNavState: NavigationState,
    onBack: () -> Unit,
    onSheetBack: () -> Unit,
    onDismissSheet: () -> Unit,
) {
    if (navState.isLoading) return

    AppNavDisplay(
        navigationState = navState,
        onBack = onBack,
        entryProvider = { key ->
            when (key) {
                is OnboardingRoute -> {
                    NavEntry(key) { OnboardingUiRoute(route = key) }
                }

                is HomeRoute,
                is AiRoute.AiAssistant,
                is SearchRoute.Search,
                is SettingsRoute.Settings -> {
                    NavEntry(key) { HomeRoot() }
                }

                else -> {
                    NavEntry(key) { Text("Unknown route: $key") }
                }
            }
        },
    )

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(sheetNavState.backStack) {
        if (sheetNavState.backStack.isEmpty()) {
            sheetState.hide()
        } else {
            sheetState.show()
        }
    }

    AnimatedVisibility(
        visible = sheetNavState.backStack.isNotEmpty(),
    ) {
        ModalBottomSheet(
            onDismissRequest = onDismissSheet,
            sheetState = sheetState,
            dragHandle = null,
        ) {
            when (val currentSheet = sheetNavState.currentRoute) {
                is HomeRoute -> {
                    HomeSheetUi(
                        route = currentSheet,
                        onBack = onSheetBack,
                    )
                }

                else -> {}
            }
        }
    }
}
