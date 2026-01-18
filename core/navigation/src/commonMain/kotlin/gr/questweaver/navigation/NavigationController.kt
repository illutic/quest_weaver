package gr.questweaver.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationController {

    private val _state = MutableStateFlow(NavigationState.Default)
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    fun navigateTo(route: Route) {
        val newBackStack = if (route.popBackStack) listOf(route) else _state.value.backStack + route
        _state.update { it.copy(backStack = newBackStack, currentRoute = route) }
    }

    fun navigateBack() {
        _state.update {
            if (it.backStack.isNotEmpty()) {
                val newStack = it.backStack.dropLast(1)
                it.copy(backStack = newStack, currentRoute = newStack.lastOrNull())
            } else {
                it
            }
        }
    }

    fun dismissSheet() {
        _state.update {
            val newStack = it.backStack.dropLastWhile { route -> route is SheetRoute }
            it.copy(backStack = newStack, currentRoute = newStack.lastOrNull())
        }
    }

    fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }
}
