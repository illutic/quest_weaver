import SwiftUI
import Shared

struct HomeDashboardView: View {
    let strings: HomeStrings
    let state: HomeState
    let viewModel: HomeViewModel

    // Navigation Callbacks
    let onRecentGamesViewAllClick: () -> Void
    let onResourceClick: (String) -> Void
    let onResourcesViewAllClick: () -> Void

    // Animation States
    @State private var isWelcomeVisible = false
    @State private var isRecentGamesVisible = false
    @State private var isQuickActionsVisible = false
    @State private var isResourcesVisible = false

    var body: some View {
        ScrollView {
            VStack(spacing: Theme.Dimens.spacing4) {
                WelcomeSection(strings: strings)
                    .opacity(isWelcomeVisible ? 1 : 0)
                    .offset(y: isWelcomeVisible ? 0 : 20)

                RecentGamesSection(
                    strings: strings,
                    games: state.recentGames,
                    onGameClick: { viewModel.onEvent(event: HomeEventOnGameClick(gameId: $0)) },
                    onViewAllClick: { onRecentGamesViewAllClick() }
                )
                    .opacity(isRecentGamesVisible ? 1 : 0)
                    .offset(y: isRecentGamesVisible ? 0 : 20)

                QuickActionsSection(
                    strings: strings,
                    onCreateGameClick: { viewModel.onEvent(event: HomeEventOnCreateGameClick.shared) },
                    onJoinGameClick: { viewModel.onEvent(event: HomeEventOnJoinGameClick.shared) }
                )
                    .opacity(isQuickActionsVisible ? 1 : 0)
                    .offset(y: isQuickActionsVisible ? 0 : 20)

                ResourcesSection(
                    strings: strings,
                    resources: state.resources,
                    onAiAssistantClick: { viewModel.onEvent(event: HomeEventOnAiAssistantClick.shared) },
                    onResourceClick: { onResourceClick($0) },
                    onViewAllClick: { onResourcesViewAllClick() }
                )
                    .opacity(isResourcesVisible ? 1 : 0)
                    .offset(y: isResourcesVisible ? 0 : 20)
            }
            .padding(.vertical, Theme.Dimens.spacing4)
            .adaptive()
        }
        .onAppear {
            withAnimation(.easeOut(duration: 0.5)) {
                isWelcomeVisible = true
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.1)) {
                isRecentGamesVisible = true
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.2)) {
                isQuickActionsVisible = true
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.3)) {
                isResourcesVisible = true
            }
        }
        .navigationTitle(strings.homeTitle)
    }
}
