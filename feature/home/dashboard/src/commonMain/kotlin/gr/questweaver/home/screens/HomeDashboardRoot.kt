package gr.questweaver.home.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import gr.questweaver.home.HomeDashboardViewModel
import gr.questweaver.home.HomeEvent
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.RecentGamesScreen
import gr.questweaver.home.ResourcesListScreen
import gr.questweaver.navigation.AppNavDisplay

@Composable
fun HomeDashboardRoute() {
    val viewModel = viewModel<HomeDashboardViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val navState by viewModel.homeNavigationState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    AppNavDisplay(
        navigationState = navState,
        onBack = { viewModel.onEvent(HomeEvent.OnBackClick) },
    ) { key ->
        when (key) {
            is HomeRoute.Home -> NavEntry(key) {
                HomeDashboardScreen(
                    state = state,
                    snackbarHostState = snackbarHostState,
                    onJoinGameClick = { viewModel.onEvent(HomeEvent.OnJoinGameClick) },
                    onCreateGameClick = { viewModel.onEvent(HomeEvent.OnCreateGameClick) },
                    onGameClick = { viewModel.onEvent(HomeEvent.OnGameClick(it)) },
                    onRecentGamesViewAllClick = {
                        viewModel.onEvent(HomeEvent.OnRecentGamesViewAllClick)
                    },
                    onAiAssistantClick = { viewModel.onEvent(HomeEvent.OnAiAssistantClick) },
                    onResourceClick = { viewModel.onEvent(HomeEvent.OnResourceClick(it)) },
                    onResourcesViewAllClick = {
                        viewModel.onEvent(HomeEvent.OnResourcesViewAllClick)
                    }
                )
            }

            HomeRoute.RecentGames -> NavEntry(key) {
                RecentGamesScreen(
                    state = state,
                    onGameClick = { viewModel.onEvent(HomeEvent.OnGameClick(it)) },
                    onBackClick = { viewModel.onEvent(HomeEvent.OnBackClick) }
                )
            }

            HomeRoute.ResourcesList -> NavEntry(key) {
                ResourcesListScreen(
                    state = state,
                    onBack = { viewModel.onEvent(HomeEvent.OnBackClick) },
                    onResourceClick = { viewModel.onEvent(HomeEvent.OnResourceClick(it)) }
                )
            }

            else -> NavEntry(key) {}
        }
    }
}
