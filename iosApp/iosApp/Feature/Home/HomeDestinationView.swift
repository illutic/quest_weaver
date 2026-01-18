//
//  HomeDestinationView.swift
//  iosApp
//
//  Created by Quest Weaver on 2026-01-16.
//

import SwiftUI
import Shared

struct HomeDestinationView: View {
    let route: Route
    let state: HomeState
    let viewModel: HomeViewModel

    var body: some View {
        switch route {
        case is HomeRouteHome:
            HomeDashboardView(
                strings: state.strings,
                state: state,
                viewModel: viewModel,
                )

        case is HomeRouteRecentGames:
            RecentGamesView(
                strings: state.strings,
                games: state.recentGames,
                onGameClick: { viewModel.onEvent(event: HomeEventOnGameClick(gameId: $0)) }
            )

        case is HomeRouteResourcesList:
            ResourcesView(
                strings: state.strings,
                resources: state.resources,
                onResourceClick: { resourceId in
                    viewModel.onEvent(event: HomeEventOnResourceClick(resourceId: resourceId))
                }
            )

        case is HomeRouteSearch:
            SearchPlaceholderView()

        case is HomeRouteSettings:
            SettingsPlaceholderView()

        default:
            EmptyView()
        }
    }
}
