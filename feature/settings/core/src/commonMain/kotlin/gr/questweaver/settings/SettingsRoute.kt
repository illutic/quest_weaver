package gr.questweaver.settings

import gr.questweaver.navigation.Route
import kotlinx.serialization.Serializable

sealed interface SettingsRoute : Route {

    @Serializable
    data object Settings : SettingsRoute {
        override val id: String = "settings"
        override val path: String = "settings"
        override val popBackStack: Boolean = true
    }
}
