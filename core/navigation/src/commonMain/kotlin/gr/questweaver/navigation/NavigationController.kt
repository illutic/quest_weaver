package gr.questweaver.navigation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationController {
    private val _navigationState = MutableStateFlow(NavigationState())
    val state = _navigationState.asStateFlow()

    fun navigateTo(route: Route) {
        _navigationState.update { currentState ->
            val newBackStack = if (route.popBackStack) {
                listOf(route)
            } else {
                currentState.backStack + route
            }
            currentState.copy(backStack = newBackStack)
        }
    }

    fun setLoading(isLoading: Boolean) {
        _navigationState.update { currentState ->
            currentState.copy(isLoading = isLoading)
        }
    }

    fun navigateBack() {
        _navigationState.update { currentState ->
            if (currentState.backStack.isEmpty()) return@update currentState
            val newBackStack = currentState.backStack.dropLast(1)
            currentState.copy(backStack = newBackStack)
        }
    }

    fun clearBackStack() {
        _navigationState.update { currentState ->
            currentState.copy(backStack = emptyList())
        }
    }
}