package gr.questweaver.bottombar

import gr.questweaver.navigation.Route

sealed interface BottomBarEvent {
    data class OnItemClick(val route: Route) : BottomBarEvent
    data object OnBackClick : BottomBarEvent
}
