package gr.questweaver.home

import gr.questweaver.navigation.Route
import kotlinx.serialization.Serializable

sealed interface HomeRoute : Route {
    @Serializable
    data object Graph : HomeRoute {
        override val path: String = "home_graph"
        override val id: String = "home_graph"
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
}
