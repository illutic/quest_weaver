package gr.questweaver.navigation

data class NavigationState(
    val backStack: List<Route> = emptyList(),
    val isLoading: Boolean = false,
) {
    val currentRoute: Route? = backStack.lastOrNull()
    companion object {
        val Default = NavigationState()
    }
}
