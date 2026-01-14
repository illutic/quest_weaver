package gr.questweaver.home.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import gr.questweaver.core.ui.sizes
import gr.questweaver.home.HomeSideEffect
import gr.questweaver.home.HomeState
import gr.questweaver.home.HomeViewModel
import gr.questweaver.home.components.QuickActionsSection
import gr.questweaver.home.components.RecentGamesSection
import gr.questweaver.home.components.ResourcesSection
import gr.questweaver.home.components.WelcomeSection
import gr.questweaver.navigation.Route

@Composable
fun HomeUiRoute(
    onNavigate: (Route) -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    val state by viewModel.state.collectAsState()
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

    HomeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onCreateGameClick = viewModel::onCreateGameClick,
        onJoinGameClick = viewModel::onJoinGameClick,
        onGameClick = viewModel::onGameClick,
        onRecentGamesViewAllClick = viewModel::onRecentGamesViewAllClick,
        onAiAssistantClick = viewModel::onAiAssistantClick,
        onResourceClick = viewModel::onResourceClick,
        onResourcesViewAllClick = viewModel::onResourcesViewAllClick
    )
}

@Composable
fun HomeScreen(
    state: HomeState,
    snackbarHostState: SnackbarHostState,
    onCreateGameClick: () -> Unit,
    onJoinGameClick: () -> Unit,
    onGameClick: (String) -> Unit,
    onRecentGamesViewAllClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onResourceClick: (String) -> Unit,
    onResourcesViewAllClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        BoxWithConstraints(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            val isWideScreen = maxWidth > 600.dp

            // Animation State
            val visibleState = remember { MutableTransitionState(false) }
            LaunchedEffect(Unit) { visibleState.targetState = true }

            if (isWideScreen) {
                HomeContentDesktop(
                    state = state,
                    visibleState = visibleState,
                    onCreateGameClick = onCreateGameClick,
                    onJoinGameClick = onJoinGameClick,
                    onGameClick = onGameClick,
                    onRecentGamesViewAllClick = onRecentGamesViewAllClick,
                    onAiAssistantClick = onAiAssistantClick,
                    onResourceClick = onResourceClick,
                    onResourcesViewAllClick = onResourcesViewAllClick
                )
            } else {
                HomeContentMobile(
                    state = state,
                    visibleState = visibleState,
                    onCreateGameClick = onCreateGameClick,
                    onJoinGameClick = onJoinGameClick,
                    onGameClick = onGameClick,
                    onRecentGamesViewAllClick = onRecentGamesViewAllClick,
                    onAiAssistantClick = onAiAssistantClick,
                    onResourceClick = onResourceClick,
                    onResourcesViewAllClick = onResourcesViewAllClick
                )
            }
        }
    }
}

@Composable
private fun HomeContentMobile(
    state: HomeState,
    visibleState: MutableTransitionState<Boolean>,
    onCreateGameClick: () -> Unit,
    onJoinGameClick: () -> Unit,
    onGameClick: (String) -> Unit,
    onRecentGamesViewAllClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onResourceClick: (String) -> Unit,
    onResourcesViewAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(sizes.four))

        AnimateItem(visibleState = visibleState, delay = 0) {
            WelcomeSection(strings = state.strings, modifier = Modifier.padding(bottom = sizes.one))
        }

        AnimateItem(visibleState = visibleState, delay = 100) {
            RecentGamesSection(
                strings = state.strings,
                games = state.recentGames,
                onGameClick = onGameClick,
                onViewAllClick = onRecentGamesViewAllClick,
                modifier = Modifier.padding(bottom = sizes.one)
            )
        }

        AnimateItem(visibleState = visibleState, delay = 200) {
            QuickActionsSection(
                strings = state.strings,
                onCreateGameClick = onCreateGameClick,
                onJoinGameClick = onJoinGameClick,
                modifier = Modifier.padding(bottom = sizes.one)
            )
        }

        AnimateItem(visibleState = visibleState, delay = 300) {
            ResourcesSection(
                strings = state.strings,
                resources = state.resources,
                onAiAssistantClick = onAiAssistantClick,
                onResourceClick = onResourceClick,
                onViewAllClick = onResourcesViewAllClick
            )
        }

        Spacer(modifier = Modifier.height(sizes.one))
    }
}

@Composable
private fun HomeContentDesktop(
    state: HomeState,
    visibleState: MutableTransitionState<Boolean>,
    onCreateGameClick: () -> Unit,
    onJoinGameClick: () -> Unit,
    onGameClick: (String) -> Unit,
    onRecentGamesViewAllClick: () -> Unit,
    onAiAssistantClick: () -> Unit,
    onResourceClick: (String) -> Unit,
    onResourcesViewAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxSize().padding(sizes.four),
        horizontalArrangement = Arrangement.spacedBy(sizes.one)
    ) {
        // Left Column: Welcome, Quick Actions, Recent Games
        Column(modifier = Modifier.weight(0.6f).verticalScroll(rememberScrollState())) {
            AnimateItem(visibleState = visibleState, delay = 0) {
                WelcomeSection(
                    strings = state.strings,
                    modifier = Modifier.padding(bottom = sizes.one)
                )
            }

            AnimateItem(visibleState = visibleState, delay = 150) {
                QuickActionsSection(
                    strings = state.strings,
                    onCreateGameClick = onCreateGameClick,
                    onJoinGameClick = onJoinGameClick,
                    modifier = Modifier.padding(bottom = sizes.one)
                )
            }

            AnimateItem(visibleState = visibleState, delay = 300) {
                RecentGamesSection(
                    strings = state.strings,
                    games = state.recentGames,
                    onGameClick = onGameClick,
                    onViewAllClick = onRecentGamesViewAllClick,
                    modifier = Modifier.padding(bottom = sizes.one)
                )
            }
        }

        // Right Column: Resources
        Column(modifier = Modifier.weight(0.4f).verticalScroll(rememberScrollState())) {
            AnimateItem(visibleState = visibleState, delay = 450) {
                ResourcesSection(
                    strings = state.strings,
                    resources = state.resources,
                    onAiAssistantClick = onAiAssistantClick,
                    onResourceClick = onResourceClick,
                    onViewAllClick = onResourcesViewAllClick
                )
            }
        }
    }
}

@Composable
private fun AnimateItem(
    visibleState: MutableTransitionState<Boolean>,
    delay: Int,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visibleState = visibleState,
        enter =
            fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = delay)) +
                    slideInVertically(
                        animationSpec = tween(durationMillis = 500, delayMillis = delay)
                    ) { it / 2 }
    ) { content() }
}
