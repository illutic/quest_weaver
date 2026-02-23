import SwiftUI
import Shared

struct HomeDashboardView: View {
    let onGameClick: (String) -> Void
    let onGameViewAllClick: () -> Void
    let onCreateGameClick: () -> Void
    let onJoinGameClick: () -> Void
    let onAiClick: () -> Void
    let onResourceClick: (String) -> Void
    let onResourceViewAllClick: () -> Void

    // Animation States
    // isWelcomeVisible removed
    @State var state: HomeState
    @State private var isRecentGamesVisible = false
    @State private var isQuickActionsVisible = false
    @State private var isResourcesVisible = false

    var body: some View {
        ScrollView {
            VStack(spacing: Theme.Dimens.spacing4) {
                // WelcomeSection removed

                RecentGamesSection(
                    strings: state.strings,
                    games: state.recentGames,
                    onGameClick: onGameClick,
                    onViewAllClick: onGameViewAllClick
                )
                    .opacity(isRecentGamesVisible ? 1 : 0)
                    .offset(y: isRecentGamesVisible ? 0 : 20)

                QuickActionsSection(
                    strings: state.strings,
                    onCreateGameClick: onCreateGameClick,
                    onJoinGameClick: onJoinGameClick
                )
                    .opacity(isQuickActionsVisible ? 1 : 0)
                    .offset(y: isQuickActionsVisible ? 0 : 20)

                ResourcesSection(
                    strings: state.strings,
                    resources: state.resources,
                    onAiAssistantClick: onAiClick,
                    onResourceClick: onResourceClick,
                    onViewAllClick: onResourceViewAllClick
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
        .navigationTitle(state.strings.welcomeSubtitle)
    }
}
