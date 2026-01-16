package gr.questweaver.home

import gr.questweaver.navigation.Route
import gr.questweaver.navigation.SheetRoute
import kotlinx.serialization.Serializable

sealed interface HomeRoute : Route {
    @Serializable
    data object Graph : HomeRoute {
        override val path: String = "home_graph"
        override val id: String = "home_graph"
        override val popBackStack = true
    }

    @Serializable
    data object Home : HomeRoute {
        override val path: String = "home"
        override val id: String = "home"
    }

    @Serializable
    data object RecentGames : HomeRoute {
        override val path: String = "recent_games"
        override val id: String = "recent_games"
    }

    @Serializable
    data object ResourcesList : HomeRoute {
        override val path: String = "resources_list"
        override val id: String = "resources_list"
    }

    @Serializable
    data object Search : HomeRoute {
        override val path: String = "search"
        override val id: String = "search"
    }

    @Serializable
    data object Settings : HomeRoute {
        override val path: String = "settings"
        override val id: String = "settings"
    }

    @Serializable
    data class ResourceDetails(val resourceId: String, val title: String) : SheetRoute {
        override val path: String = "resource_sheet/$resourceId?title=$title"
        override val id: String = resourceId
    }

    @Serializable
    data object CreateGame : SheetRoute {
        override val path: String = "create_game"
        override val id: String = "create_game"
    }
}
