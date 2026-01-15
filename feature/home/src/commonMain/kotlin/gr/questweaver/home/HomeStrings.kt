package gr.questweaver.home

import gr.questweaver.feature.home.Res
import gr.questweaver.feature.home.home_ai_assistant_description
import gr.questweaver.feature.home.home_ai_assistant_title
import gr.questweaver.feature.home.home_create_game_button
import gr.questweaver.feature.home.home_game_live
import gr.questweaver.feature.home.home_game_offline
import gr.questweaver.feature.home.home_game_players
import gr.questweaver.feature.home.home_join_game_button
import gr.questweaver.feature.home.home_quick_actions_title
import gr.questweaver.feature.home.home_recent_games_empty
import gr.questweaver.feature.home.home_recent_games_title
import gr.questweaver.feature.home.home_recent_games_view_all
import gr.questweaver.feature.home.home_resources_title
import gr.questweaver.feature.home.home_resources_view_all
import gr.questweaver.feature.home.home_title
import gr.questweaver.feature.home.home_welcome_subtitle
import gr.questweaver.feature.home.home_welcome_title
import org.jetbrains.compose.resources.getString

data class HomeStrings(
    val homeTitle: String = "",
    val welcomeTitle: String = "",
    val welcomeSubtitle: String = "",
    val recentGamesTitle: String = "",
    val recentGamesViewAll: String = "",
    val recentGamesEmpty: String = "",
    val quickActionsTitle: String = "",
    val createGameButton: String = "",
    val joinGameButton: String = "",
    val usefulResourcesTitle: String = "",
    val usefulResourcesViewAll: String = "",
    val aiAssistantTitle: String = "",
    val aiAssistantDescription: String = "",
    val gameLive: String = "",
    val gameOffline: String = "",
    val gamePlayers: String = ""
) {
    companion object {
        val Empty = HomeStrings()

        suspend fun load(): HomeStrings =
            HomeStrings(
                homeTitle = getString(Res.string.home_title),
                welcomeTitle = getString(Res.string.home_welcome_title),
                welcomeSubtitle = getString(Res.string.home_welcome_subtitle),
                recentGamesTitle = getString(Res.string.home_recent_games_title),
                recentGamesViewAll =
                    getString(Res.string.home_recent_games_view_all),
                recentGamesEmpty = getString(Res.string.home_recent_games_empty),
                quickActionsTitle = getString(Res.string.home_quick_actions_title),
                createGameButton = getString(Res.string.home_create_game_button),
                joinGameButton = getString(Res.string.home_join_game_button),
                usefulResourcesTitle = getString(Res.string.home_resources_title),
                usefulResourcesViewAll =
                    getString(Res.string.home_resources_view_all),
                aiAssistantTitle = getString(Res.string.home_ai_assistant_title),
                aiAssistantDescription =
                    getString(Res.string.home_ai_assistant_description),
                gameLive = getString(Res.string.home_game_live),
                gameOffline = getString(Res.string.home_game_offline),
                gamePlayers = getString(Res.string.home_game_players)
            )
    }
}
