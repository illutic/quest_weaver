package gr.questweaver.home

suspend fun loadHomeStrings(): HomeStrings =
    HomeStrings(
        homeTitle = "Home",
        welcomeTitle = "Welcome back, Adventurer!",
        welcomeSubtitle = "Continue your epic journey!",
        recentGamesTitle = "Recent games",
        recentGamesViewAll = "View all",
        recentGamesEmpty = "Your recent games will appear here.",
        quickActionsTitle = "Quick Actions",
        createGameButton = "Create Game",
        joinGameButton = "Join Game",
        usefulResourcesTitle = "Tools & Useful resources",
        usefulResourcesViewAll = "View all",
        aiAssistantTitle = "AI Assistant",
        aiAssistantDescription = "Generate content for your game.",
        gameLive = "Live",
        gameOffline = "Offline",
        gamePlayers = "%s players",
        createGameModalTitle = "Create a game",
        createGameInputLabel = "Game title",
        createGameCampaign = "Campaign",
        createGameOneShot = "One shot",
        startGameButton = "Start game",
        cancelButton = "Cancel"
    )
