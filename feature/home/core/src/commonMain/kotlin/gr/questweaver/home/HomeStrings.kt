package gr.questweaver.home

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
        val Default = HomeStrings()
        val Empty = HomeStrings()
    }
}
