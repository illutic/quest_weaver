//
//  HomeView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct HomeView: View {
    let onNavigateGlobal: (Route) -> Void

    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: HomeState = HomeState(
        isLoading: false,
        strings: HomeStrings.companion.Empty,
        recentGames: [],
        resources: [],
        )
    @State private var toastMessage: String? = nil

    // Animation States
    @State private var isWelcomeVisible = false
    @State private var isRecentGamesVisible = false
    @State private var isQuickActionsVisible = false
    @State private var isResourcesVisible = false

    var body: some View {
        let viewModel: HomeViewModel = viewModelStoreOwner.viewModel(
            factory: HomeViewModel.companion.createFactory()
        )

        ZStack {
            Theme.Colors.background.ignoresSafeArea()

            ScrollView {
                VStack(spacing: Theme.Dimens.spacing4) {
                    WelcomeSection(strings: state.strings)
                        .opacity(isWelcomeVisible ? 1 : 0)
                        .offset(y: isWelcomeVisible ? 0 : 20)

                    RecentGamesSection(
                        strings: state.strings,
                        games: state.recentGames,
                        onGameClick: { viewModel.onGameClick(gameId: $0) },
                        onViewAllClick: { viewModel.onRecentGamesViewAllClick() }
                    )
                        .opacity(isRecentGamesVisible ? 1 : 0)
                        .offset(y: isRecentGamesVisible ? 0 : 20)

                    QuickActionsSection(
                        strings: state.strings,
                        onCreateGameClick: { viewModel.onCreateGameClick() },
                        onJoinGameClick: { viewModel.onJoinGameClick() }
                    )
                        .opacity(isQuickActionsVisible ? 1 : 0)
                        .offset(y: isQuickActionsVisible ? 0 : 20)

                    ResourcesSection(
                        strings: state.strings,
                        resources: state.resources,
                        onAiAssistantClick: { viewModel.onAiAssistantClick() },
                        onResourceClick: { viewModel.onResourceClick(resourceId: $0) },
                        onViewAllClick: { viewModel.onResourcesViewAllClick() }
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

            if let message = toastMessage {
                VStack {
                    Spacer()
                    Text(message)
                        .padding()
                        .background(Theme.Colors.surface.opacity(0.9))
                        .foregroundColor(Theme.Colors.onBackground)
                        .cornerRadius(8)
                        .shadow(radius: 4)
                        .padding(.bottom, 50)
                        .transition(.move(edge: .bottom).combined(with: .opacity))
                        .onAppear {
                            DispatchQueue.main.asyncAfter(deadline: .now() + 2) {
                                withAnimation {
                                    toastMessage = nil
                                }
                            }
                        }
                }
                .zIndex(1)
            }
        }
        .task {
            // Subscribe to State
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { (state: HomeState?) in
                    if let s = state {
                        self.state = s
                    }
                }
            )

            // Subscribe to Side Effects
            viewModel.sideEffects.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { (effect: HomeSideEffect?) in
                    if let showToast = effect as? HomeSideEffectShowToast {
                        withAnimation {
                            self.toastMessage = showToast.message
                        }
                    }
                }
            )
        }
    }
}

#Preview {
    HomeView(onNavigateGlobal: { _ in })
}
