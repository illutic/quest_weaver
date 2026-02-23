package gr.questweaver.home.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import gr.questweaver.ai.AiRoute
import gr.questweaver.ai.screens.AiAssistantScreen
import gr.questweaver.core.components.AppBottomBar
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.screens.HomeDashboardRoute
import gr.questweaver.navigation.AppNavDisplay
import gr.questweaver.search.SearchRoute
import gr.questweaver.search.screens.SearchScreen
import gr.questweaver.settings.SettingsRoute
import gr.questweaver.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun HomeRoot() {
    val viewModel =
        viewModel<HomeNavigationViewModel>(factory = HomeNavigationViewModel.createFactory())
    val state by viewModel.appNavState.collectAsStateWithLifecycle()

    Scaffold(
        contentWindowInsets = WindowInsets.navigationBars,
        bottomBar = {
            AppBottomBar(
                navItems = viewModel.navItems,
                selectedNavIndex = viewModel.navItems.indexOfFirst { it.route == state.currentRoute },
                onNavigate = { route -> viewModel.onNavigate(route) }
            )
        }
    ) {
        AppNavDisplay(
            modifier = Modifier.padding(it),
            navigationState = state,
            onBack = { viewModel.onBack() },
            entryProvider = { key ->
                when (key) {
                    is HomeRoute -> {
                        NavEntry(key) { HomeDashboardRoute() }
                    }

                    is AiRoute.AiAssistant -> {
                        NavEntry(key) { AiAssistantScreen() }
                    }

                    is SearchRoute.Search -> {
                        NavEntry(key) { SearchScreen() }
                    }

                    is SettingsRoute.Settings -> {
                        NavEntry(key) { SettingsScreen() }
                    }

                    else -> {
                        NavEntry(key) { Text("Unknown route: $key") }
                    }
                }
            },
        )
    }
}
