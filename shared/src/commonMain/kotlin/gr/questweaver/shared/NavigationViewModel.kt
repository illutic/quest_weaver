package gr.questweaver.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.home.HomeRoute
import gr.questweaver.navigation.NavigationController
import gr.questweaver.navigation.NavControllerType
import gr.questweaver.navigation.Route
import gr.questweaver.onboarding.OnboardingRoute
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class NavigationViewModel : ViewModel(), KoinComponent {
    private val isUserRegisteredUseCase: IsUserRegisteredUseCase by inject()
    private val appNavController: NavigationController by inject(named(NavControllerType.App))
    private val sheetNavController: NavigationController by inject(named(NavControllerType.Sheet))

    val navigationState = appNavController.state
    val sheetState = sheetNavController.state

    fun navigateTo(route: Route) {
        appNavController.navigateTo(route)
    }

    fun navigateBack() {
        appNavController.navigateBack()
    }

    fun dismissSheet() {
        sheetNavController.clearBackStack()
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
        appNavController.setLoading(isLoading)
    }

    init {
        viewModelScope.launch { load() }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { NavigationViewModel() } }
    }
}
