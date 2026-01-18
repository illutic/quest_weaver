package gr.questweaver.search

import gr.questweaver.navigation.Route

sealed interface SearchRoute : Route {
    data object Search : SearchRoute {
        override val id: String = "search"
        override val path: String = "search"
        override val popBackStack: Boolean = true
    }
}
