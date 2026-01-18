package gr.questweaver.home.screens

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.home.HomeEvent
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeViewModel
import gr.questweaver.home.RecentGamesScreen
import gr.questweaver.home.ResourcesListScreen

@Composable
fun HomeUiRoute(route: HomeRoute, viewModel: HomeViewModel = viewModel { HomeViewModel() }) {
    val state by viewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    when (route) {
        HomeRoute.Home -> {
            HomeScreen(
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

        HomeRoute.RecentGames -> {
            RecentGamesScreen(
                state = state,
                onGameClick = { viewModel.onEvent(HomeEvent.OnGameClick(it)) },
                onBackClick = { viewModel.onEvent(HomeEvent.OnBackClick) }
            )
        }

        HomeRoute.ResourcesList -> {
            ResourcesListScreen(
                state = state,
                onResourceClick = { viewModel.onEvent(HomeEvent.OnResourceClick(it)) }
            )
        }

        HomeRoute.Search -> SearchScreen()
        HomeRoute.Settings -> SettingsScreen()
        else -> {}
    }
}
