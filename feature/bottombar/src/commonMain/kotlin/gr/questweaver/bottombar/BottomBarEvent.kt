package gr.questweaver.bottombar

import gr.questweaver.navigation.Route

sealed interface BottomBarEvent {
    data class OnItemClick(val route: Route) : BottomBarEvent
    data object OnBackClick : BottomBarEvent
    data class OnInputChanged(val value: String) : BottomBarEvent
    data object OnSubmitClick : BottomBarEvent
}
