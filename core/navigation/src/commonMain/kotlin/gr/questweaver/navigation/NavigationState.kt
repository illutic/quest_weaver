package gr.questweaver.navigation

data class NavigationState(
    val backStack: List<Route> = emptyList(),
    val currentRoute: Route? = null,
    val isLoading: Boolean = false,
)
