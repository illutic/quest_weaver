package gr.questweaver.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationController {

    private val _state = MutableStateFlow(NavigationState.Default)
    val state: StateFlow<NavigationState> = _state.asStateFlow()

    fun navigateTo(route: Route) {
        _state.update { it.copy(backStack = it.backStack + route, currentRoute = route) }
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

    fun setLoading(isLoading: Boolean) {
        _state.update { it.copy(isLoading = isLoading) }
    }
}
