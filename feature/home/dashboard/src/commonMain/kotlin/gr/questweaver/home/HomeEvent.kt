package gr.questweaver.home

sealed interface HomeEvent {
    data class OnGameClick(val gameId: String) : HomeEvent
    data object OnCreateGameClick : HomeEvent
    data object OnJoinGameClick : HomeEvent
    data object OnRecentGamesViewAllClick : HomeEvent
    data object OnResourcesViewAllClick : HomeEvent
    data object OnAiAssistantClick : HomeEvent
    data class OnResourceClick(val resourceId: String) : HomeEvent
    data class OnBottomNavClick(val route: HomeRoute) : HomeEvent
    data object OnBackClick : HomeEvent
    data object OnDismissSheet : HomeEvent
}
