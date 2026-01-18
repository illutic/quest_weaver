package gr.questweaver.bottombar

import gr.questweaver.navigation.Route

data class BottomBarState(
    val items: List<BottomBarItem> = emptyList(),
    val mode: BottomBarMode = BottomBarMode.Empty,
    val inputValue: String = "",
    val showBackButton: Boolean = false
)

sealed interface BottomBarMode {
    data object Standard : BottomBarMode
    data class TextField(val placeholder: String) : BottomBarMode
    data object Empty : BottomBarMode
}

data class BottomBarItem(
    val label: String,
    val icon: BottomBarIcon,
    val route: Route,
    val selected: Boolean,
    val performDefaultNavigation: Boolean = true
)

enum class BottomBarIcon {
    Home,
    Search,
    Settings,
    Back
}
