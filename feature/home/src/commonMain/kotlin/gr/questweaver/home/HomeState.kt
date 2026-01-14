package gr.questweaver.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val isLoading: Boolean = false,
    val strings: HomeStrings = HomeStrings.Empty,
    val recentGames: ImmutableList<GameSession> = persistentListOf(),
    val resources: ImmutableList<Resource> = persistentListOf()
)

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
    val type: ResourceType
)

enum class ResourceType {
    Rulebook,
    CharacterSheet,
    Spellbook,
    ItemHeader
}
