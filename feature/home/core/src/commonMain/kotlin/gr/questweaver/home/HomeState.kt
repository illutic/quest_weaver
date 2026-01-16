package gr.questweaver.home

import gr.questweaver.navigation.Route
import gr.questweaver.navigation.SheetRoute
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SheetUiState(
    val backStack: List<SheetRoute> = emptyList(),
) {
    val title =
        when (val route = backStack.lastOrNull()) {
            is HomeRoute.ResourceDetails -> route.title
            else -> ""
        }
}

data class HomeState(
    val isLoading: Boolean = false,
    val recentGames: ImmutableList<GameSession> = persistentListOf(),
    val resources: ImmutableList<Resource> = persistentListOf(),
    val selectedResource: Resource? = null,
    val backStack: List<Route> = listOf(HomeRoute.Home),
    val sheet: SheetUiState = SheetUiState(),
    val strings: HomeStrings = HomeStrings.Default
) {
    val currentRoute: Route
        get() = backStack.lastOrNull() ?: HomeRoute.Home

    companion object {
        val Default = HomeState()
    }
}

data class GameSession(
    val id: String,
    val title: String,
    val type: String, // e.g. "Campaign", "One shot"
    val level: Int,
    val master: String, // Kept for now, though not explicitly in new design (maybe implicit)
    val players: Int,
    val isLive: Boolean
)

data class Resource(
    val id: String,
    val title: String,
    val description: String,
    val type: ResourceType,
    val imageUrl: String? = null
)

enum class ResourceType {
    Rulebook,
    CharacterSheet,
    Spellbook,
    ItemHeader
}
