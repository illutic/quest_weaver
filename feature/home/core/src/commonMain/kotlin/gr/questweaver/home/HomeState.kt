package gr.questweaver.home

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class HomeState(
    val isLoading: Boolean = false,
    val recentGames: ImmutableList<GameSession> = persistentListOf(),
    val resources: ImmutableList<Resource> = persistentListOf(),
    val selectedResource: Resource? = null,
    val strings: HomeStrings = HomeStrings.Default
) {
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

enum class GameType(val displayName: String) {
    Campaign("Campaign"),
    OneShot("One shot")
}
