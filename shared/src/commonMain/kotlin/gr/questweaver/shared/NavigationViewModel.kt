package gr.questweaver.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.home.HomeRoute
import gr.questweaver.navigation.NavigationState
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NavigationViewModel(
    private val isUserRegisteredUseCase: IsUserRegisteredUseCase,
) : ViewModel() {
    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState = _navigationState.asStateFlow()

    fun navigateTo(route: Route) {
        _navigationState.update {
            val newBackStack = if (route.popBackStack) {
                listOf(route)
            } else {
                it.backStack + route
            }

            NavigationState(backStack = newBackStack, currentRoute = route)
        }
    }

    fun navigateBack() {
        _navigationState.update {
            if (it.backStack.size <= 1) return@update it
            val newBackStack = it.backStack.dropLast(1)
            NavigationState(backStack = newBackStack, currentRoute = newBackStack.last())
        }
    }

    private suspend fun load() {
        setLoading(true)
        val isRegistered = isUserRegisteredUseCase().getOrElse { false }
        if (isRegistered) {
            navigateTo(HomeRoute.Graph)
        } else {
            navigateTo(OnboardingRoute.Graph)
        }
        setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        _navigationState.update { it.copy(isLoading = isLoading) }
    }

    init {
        viewModelScope.launch { load() }
    }

    companion object {
        fun createFactory(isUserRegisteredUseCase: IsUserRegisteredUseCase) =
            viewModelFactory {
                initializer { NavigationViewModel(isUserRegisteredUseCase) }
            }
    }
}
