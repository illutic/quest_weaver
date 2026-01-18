package gr.questweaver.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.home.HomeRoute
import gr.questweaver.navigation.NavigationController
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NavigationViewModel : ViewModel(), KoinComponent {
    private val isUserRegisteredUseCase: IsUserRegisteredUseCase by inject()
    private val navigationController: NavigationController by inject()

    val navigationState = navigationController.state

    fun navigateTo(route: Route) {
        navigationController.navigateTo(route)
    }

    fun navigateBack() {
        navigationController.navigateBack()
    }

    fun dismissSheet() {
        navigationController.dismissSheet()
    }

    private suspend fun load() {
        setLoading(true)
        val isRegistered = isUserRegisteredUseCase().getOrElse { false }
        if (isRegistered) {
            navigateTo(HomeRoute.Home)
        } else {
            navigateTo(OnboardingRoute.Welcome)
        }
        setLoading(false)
    }

    private fun setLoading(isLoading: Boolean) {
        navigationController.setLoading(isLoading)
    }

    init {
        viewModelScope.launch { load() }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { NavigationViewModel() } }
    }
}
