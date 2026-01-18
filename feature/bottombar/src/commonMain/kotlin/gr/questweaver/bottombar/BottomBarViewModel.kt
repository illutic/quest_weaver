package gr.questweaver.bottombar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.questweaver.navigation.NavigationController
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class BottomBarViewModel : ViewModel(), KoinComponent {

    private val bottomBarController: BottomBarController by inject()
    private val navigationController: NavigationController by inject()

    val state: StateFlow<BottomBarState> =
        combine(bottomBarController.state, navigationController.state) { bottomBarState, navState ->
            bottomBarState.copy(
                items = bottomBarState.items.map {
                    it.copy(selected = it.route == navState.currentRoute)
                },
                showBackButton = navState.backStack.size > 1 && bottomBarState.mode == BottomBarMode.Standard
            )
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BottomBarState())

    fun onEvent(event: BottomBarEvent) {
        when (event) {
            is BottomBarEvent.OnBackClick -> {
                bottomBarController.emitEvent(event)
                navigationController.navigateBack()
            }

            is BottomBarEvent.OnItemClick -> {
                bottomBarController.emitEvent(event)
                val item = state.value.items.find { it.route == event.route }
                // Default to true if item not found (e.g. from some other source?)
                // but event.route comes from item click usually.
                if (item == null || item.performDefaultNavigation) {
                    navigationController.navigateTo(event.route)
                }
            }

            is BottomBarEvent.OnInputChanged -> {
                bottomBarController.setInputValue(event.value)
            }

            is BottomBarEvent.OnSubmitClick -> {
                bottomBarController.emitEvent(event)
            }
        }
    }
}
