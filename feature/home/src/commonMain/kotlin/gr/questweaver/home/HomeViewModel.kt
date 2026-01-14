package gr.questweaver.home

import androidx.lifecycle.ViewModel
import gr.questweaver.navigation.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.core.component.KoinComponent

class HomeViewModel(private val navigateToCallback: ((Route) -> Unit)? = null) :
        ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun handleNavigation(route: Route) {
        navigateToCallback?.invoke(route)
    }

    companion object {
        fun createFactory(): HomeViewModel {
            return HomeViewModel()
        }
    }
}
