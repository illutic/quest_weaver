package gr.questweaver.home.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.core.components.NavItem
import gr.questweaver.home.HomeRoute
import gr.questweaver.navigation.NavControllerType
import gr.questweaver.navigation.NavigationController
import gr.questweaver.navigation.Route
import gr.questweaver.search.SearchRoute
import gr.questweaver.settings.SettingsRoute
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class HomeNavigationViewModel : ViewModel(), KoinComponent {
    private val appNavController: NavigationController by inject(named(NavControllerType.App))

    val appNavState = appNavController.state

    val navItems = listOf(
        NavItem(HomeRoute.Home, "Home", Icons.Filled.Home),
        NavItem(SearchRoute.Search, "Search", Icons.Filled.Search),
        NavItem(SettingsRoute.Settings, "Settings", Icons.Filled.Settings)
    )

    fun onNavigate(route: Route) {
        appNavController.navigateTo(route)
    }

    fun onBack() {
        appNavController.navigateBack()
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { HomeNavigationViewModel() } }
    }
}
