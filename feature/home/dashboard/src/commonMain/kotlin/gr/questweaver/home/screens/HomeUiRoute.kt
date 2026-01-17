package gr.questweaver.home.screens

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import gr.questweaver.home.HomeEvent
import gr.questweaver.home.HomeRoute
import gr.questweaver.home.HomeSideEffect
import gr.questweaver.home.HomeState
import gr.questweaver.home.HomeViewModel
import gr.questweaver.home.RecentGamesScreen
import gr.questweaver.home.ResourceDetailsScreen
import gr.questweaver.home.ResourcesListScreen
import gr.questweaver.home.SheetUiState
import gr.questweaver.home.components.HomeBottomBar
import gr.questweaver.home.create.CreateGameScreen
import gr.questweaver.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUiRoute(
    onNavigate: (Route) -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
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

    Scaffold(
        bottomBar = {
            Box(modifier = Modifier.fillMaxWidth()) {
                HomeBottomBar(
                    modifier = Modifier.align(Alignment.Center),
                    currentRoute = state.currentRoute,
                    strings = state.strings,
                    onNavigate = { route ->
                        viewModel.onEvent(HomeEvent.OnBottomNavClick(route))
                    }
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        NavDisplay(
            backStack = state.backStack,
            onBack = { viewModel.onEvent(HomeEvent.OnBackClick) },
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
                                onCreateGameClick = {
                                    viewModel.onEvent(HomeEvent.OnCreateGameClick)
                                },
                                onJoinGameClick = {
                                    viewModel.onEvent(HomeEvent.OnJoinGameClick)
                                },
                                onGameClick = {
                                    viewModel.onEvent(HomeEvent.OnGameClick(it))
                                },
                                onRecentGamesViewAllClick = {
                                    viewModel.onEvent(HomeEvent.OnRecentGamesViewAllClick)
                                },
                                onAiAssistantClick = {
                                    viewModel.onEvent(HomeEvent.OnAiAssistantClick)
                                },
                                onResourceClick = {
                                    viewModel.onEvent(HomeEvent.OnResourceClick(it))
                                },
                                onResourcesViewAllClick = {
                                    viewModel.onEvent(HomeEvent.OnResourcesViewAllClick)
                                }
                            )
                        }

                        HomeRoute.RecentGames -> {
                            RecentGamesScreen(
                                state = state,
                                onGameClick = {
                                    viewModel.onEvent(HomeEvent.OnGameClick(it))
                                },
                                onBackClick = { viewModel.onEvent(HomeEvent.OnBackClick) }
                            )
                        }

                        HomeRoute.ResourcesList -> {
                            ResourcesListScreen(
                                state = state,
                                onResourceClick = {
                                    viewModel.onEvent(HomeEvent.OnResourceClick(it))
                                },
                                onBackClick = { viewModel.onEvent(HomeEvent.OnBackClick) }
                            )
                        }

                        HomeRoute.ResourcesList -> {
                            ResourcesListScreen(
                                state = state,
                                onResourceClick = {
                                    viewModel.onEvent(HomeEvent.OnResourceClick(it))
                                },
                                onBackClick = { viewModel.onEvent(HomeEvent.OnBackClick) }
                            )
                        }

                        HomeRoute.AiAssistant -> AiAssistantScreen()
                        HomeRoute.Search -> SearchScreen()
                        HomeRoute.Settings -> SettingsScreen()
                        else -> error("Unknown Home route: $route")
                    }
                }
            }
        )
    }

    HomeUiRouteSheet(
        sheetState = state.sheet,
        onBack = { viewModel.onEvent(HomeEvent.OnDismissSheet) },
        state = state,
        onEvent = { viewModel.onEvent(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeUiRouteSheet(
    sheetState: SheetUiState,
    onBack: () -> Unit,
    state: HomeState,
    onEvent: (HomeEvent) -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    LaunchedEffect(sheetState.backStack) {
        if (sheetState.backStack.isEmpty()) {
            modalBottomSheetState.hide()
        }
    }

    if (sheetState.backStack.isEmpty()) {
        return
    }

    ModalBottomSheet(
        onDismissRequest = onBack,
        sheetState = modalBottomSheetState,
        dragHandle = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier =
                        Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val title =
                        when (val route = sheetState.backStack.lastOrNull()) {
                            is HomeRoute.ResourceDetails -> route.title
                            is HomeRoute.CreateGame -> state.strings.createGameModalTitle
                            else -> ""
                        }

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    CloseButton(onClick = onBack)
                }
                HorizontalDivider()
            }
        }
    ) {
        NavDisplay(backStack = sheetState.backStack, onBack = onBack) { route ->
            when (route) {
                is HomeRoute.ResourceDetails ->
                    NavEntry(route) {
                        ResourceDetailsScreen(
                            resource = state.selectedResource,
                        )
                    }
                is HomeRoute.CreateGame ->
                    NavEntry(route) {
                        CreateGameScreen(
                            onSubmit = { title, type ->
                                onEvent(HomeEvent.OnSubmitCreateGame(title, type))
                            },
                            strings = state.strings
                        )
                    }
                else -> error("Unknown Home Sheet route: $route")
            }
        }
    }
}

@Composable
private fun CloseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Close,
            contentDescription = "Close",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
