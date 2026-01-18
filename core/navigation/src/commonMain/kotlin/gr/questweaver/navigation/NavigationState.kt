package gr.questweaver.navigation

data class NavigationState(
    val backStack: List<Route> = emptyList(),
    val currentRoute: Route? = null,
    val isLoading: Boolean = false,
) {
    val visibleBackStack = backStack.filter { it !is SheetRoute }
    val sheetBackStack = backStack.filterIsInstance<SheetRoute>()

    companion object {
        val Default = NavigationState()
    }
}
