import SwiftUI
import Shared

struct HomeDashboardView: View {
    let strings: HomeStrings
    let state: HomeState
    let viewModel: HomeViewModel

    // Animation States
    // isWelcomeVisible removed
    @State private var isRecentGamesVisible = false
    @State private var isQuickActionsVisible = false
    @State private var isResourcesVisible = false

    var body: some View {
        ScrollView {
            VStack(spacing: Theme.Dimens.spacing4) {
                // WelcomeSection removed

                RecentGamesSection(
                    strings: strings,
                    games: state.recentGames,
                    onGameClick: { viewModel.onEvent(event: HomeEventOnGameClick(gameId: $0)) },
                    onViewAllClick: { viewModel.onEvent(event: HomeEventOnRecentGamesViewAllClick.shared) }
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
                    onResourceClick: { viewModel.onEvent(event: HomeEventOnResourceClick(resourceId: $0)) },
                    onViewAllClick: { viewModel.onEvent(event: HomeEventOnResourcesViewAllClick.shared) }
                )
                    .opacity(isResourcesVisible ? 1 : 0)
                    .offset(y: isResourcesVisible ? 0 : 20)
            }
            .padding(.vertical, Theme.Dimens.spacing4)
            .padding(.bottom, 50) // Ensure content clears TabBar
        }
        .onAppear {
            withAnimation(.easeOut(duration: 0.5)) {
                isRecentGamesVisible = true
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.1)) {
                isQuickActionsVisible = true
            }
            withAnimation(.easeOut(duration: 0.5).delay(0.2)) {
                isResourcesVisible = true
            }
        }
        .navigationTitle(strings.welcomeSubtitle)
    }
}
