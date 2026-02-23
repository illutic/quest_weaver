//
//  HomeView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct HomeView: View {
    let route: Route?
    let state: HomeState
    let onGameClick: (String) -> Void
    let onGameViewAllClick: () -> Void
    let onCreateGameClick: () -> Void
    let onJoinGameClick: () -> Void
    let onAiClick: () -> Void
    let onResourceClick: (String) -> Void
    let onResourceViewAllClick: () -> Void

    var body: some View {
        switch route {
        case is HomeRouteHome:
            HomeDashboardView(
                onGameClick: onGameClick,
                onGameViewAllClick: onGameViewAllClick,
                onCreateGameClick: onCreateGameClick,
                onJoinGameClick: onJoinGameClick,
                onAiClick: onAiClick,
                onResourceClick: onResourceClick,
                onResourceViewAllClick: onResourceViewAllClick,
                state: state,
            )

        case is HomeRouteRecentGames:
            RecentGamesView(
                strings: state.strings,
                games: state.recentGames,
                onGameClick: onGameClick
            )

        case is HomeRouteResourcesList:
            ResourcesView(
                strings: state.strings,
                resources: state.resources,
                onResourceClick: onResourceClick
            )
            
        default:
            EmptyView()
        }
    }
}

#Preview {
    HomeView(
        route: HomeRouteHome.shared,
        state: HomeState.companion.Default,
        onGameClick: { _ in },
        onGameViewAllClick: { },
        onCreateGameClick: { },
        onJoinGameClick: { },
        onAiClick: { },
        onResourceClick: { _ in },
        onResourceViewAllClick: { }
    )
}
