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
    @State private var state: HomeState = HomeState.companion.Default
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

        HomeContent(
            state: state,
            toastMessage: toastMessage,
            viewModel: viewModel,
            onToastDismiss: {
                withAnimation {
                    toastMessage = nil
                }
            }
        )
        .task {
            // Subscribe to State
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { (state: HomeState?) in
                    if let s = state {
                        withAnimation {
                            self.state = s
                        }
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

private struct HomeContent: View {
    let state: HomeState
    let toastMessage: String?
    let viewModel: HomeViewModel
    let onToastDismiss: () -> Void

    var body: some View {
        // Use shared getPathBinding helper
        let pathBinding = getPathBinding(
            backStack: state.backStack,
            currentRoute: state.backStack.last,
            onBack: { viewModel.navigateBack() }
        )

        NavigationStack(path: pathBinding) {
            ZStack {
                Theme.Colors.background.ignoresSafeArea()

                HomeDashboardView(
                    strings: state.strings,
                    state: state,
                    viewModel: viewModel
                )
            }
            .navigationDestination(for: AnyRoute.self) { anyRoute in
                HomeDestinationView(
                    route: anyRoute.base,
                    state: state,
                    viewModel: viewModel
                )
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
                                onToastDismiss()
                            }
                        }
                }
                .zIndex(1)
            }
        }
    }
}

private struct HomeDestinationView: View {
    let route: Route
    let state: HomeState
    let viewModel: HomeViewModel

    var body: some View {
        switch route {
        case is HomeRouteRecentGames:
            RecentGamesView(
                strings: state.strings,
                games: state.recentGames,
                onGameClick: { viewModel.onGameClick(gameId: $0) }
            )
        case is HomeRouteResourcesList:
            ResourcesView(
                strings: state.strings,
                resources: state.resources,
                onResourceClick: { viewModel.onResourceClick(resourceId: $0) }
            )
        default:
            EmptyView()
        }
    }
}

#Preview {
    HomeView(onNavigateGlobal: { _ in })
}
