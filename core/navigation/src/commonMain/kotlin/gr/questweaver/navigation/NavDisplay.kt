package gr.questweaver.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import androidx.navigationevent.NavigationEvent

@Composable
fun AppNavDisplay(
    navigationState: NavigationState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    entryProvider: (Route) -> NavEntry<Route>
) {
    NavDisplay(
        modifier = modifier,
        backStack = navigationState.backStack,
        onBack = onBack,
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = { edge ->
            when(edge) {
                NavigationEvent.EDGE_RIGHT -> slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it }) + fadeOut()
                NavigationEvent.EDGE_LEFT -> slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                else -> fadeIn() togetherWith fadeOut()
            }
        }
    ) { key ->
        entryProvider(key)
    }
}