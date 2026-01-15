package gr.questweaver.home.screens

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeSideEffect
import gr.questweaver.home.HomeViewModel
import gr.questweaver.navigation.Route

@Composable
fun HomeUiRoute(
    onNavigate: (Route) -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel(onNavigate) }
) {
    val state by viewModel.state.collectAsState()

    // Side Effect Handling
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(viewModel) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is HomeSideEffect.ShowToast -> {
                    snackbarHostState.showSnackbar(effect.message)
                }
            }
        }
    }

    NavDisplay(
        backStack = state.backStack,
        onBack = { viewModel.navigateBack() },
        transitionSpec = {
            slideInHorizontally { width -> width } togetherWith
                    slideOutHorizontally { width -> -width }
        },
        popTransitionSpec = {
            slideInHorizontally { width -> -width } togetherWith
                    slideOutHorizontally { width -> width }
        },
        predictivePopTransitionSpec = {
            slideInHorizontally { width -> -width } togetherWith
                    slideOutHorizontally { width -> width }
        },
        entryProvider = { route ->
            NavEntry(route) {
                when (route) {
                    HomeRoute.Home -> {
                        HomeScreen(
                            state = state,
                            snackbarHostState = snackbarHostState,
                            onCreateGameClick = viewModel::onCreateGameClick,
                            onJoinGameClick = viewModel::onJoinGameClick,
                            onGameClick = viewModel::onGameClick,
                            onRecentGamesViewAllClick =
                                viewModel::onRecentGamesViewAllClick,
                            onAiAssistantClick = viewModel::onAiAssistantClick,
                            onResourceClick = viewModel::onResourceClick,
                            onResourcesViewAllClick = viewModel::onResourcesViewAllClick
                        )
                    }

                    HomeRoute.RecentGames -> {
                        RecentGamesScreen(
                            state = state,
                            onGameClick = viewModel::onGameClick,
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }

                    HomeRoute.ResourcesList -> {
                        ResourcesListScreen(
                            state = state,
                            onResourceClick = viewModel::onResourceClick,
                            onBackClick = { viewModel.navigateBack() }
                        )
                    }

                    else -> error("Unknown Home route: $route")
                }
            }
        }
    )
}
