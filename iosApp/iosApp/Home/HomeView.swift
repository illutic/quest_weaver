//
//  HomeView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct HomeView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: HomeState = HomeState.companion.Default

    // For UiState, we might not have a static Default, so we can use an optional or construct one.
    // Assuming defaults work, but to be safe with Swift/KMP interop without checking generated header:
    @State private var toastMessage: String? = nil

    var body: some View {
        let viewModel: HomeViewModel = viewModelStoreOwner.viewModel(
            factory: HomeViewModel.Companion.shared.createFactory()
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
            // Subscribe to State (now includes UI/Nav state)
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
        // Binding for the Navigation Stack
        let pathBinding = getPathBinding(
            backStack: state.backStack,
            onBack: { viewModel.onEvent(event: HomeEventOnBackClick.shared) }
        )

        // Binding for the Sheet Presentation (Root)
        let sheetRootBinding = getSheetRootBinding(
            sheetBackStack: state.sheet.backStack,
            onDismiss: { viewModel.onEvent(event: HomeEventOnDismissSheet.shared) }
        )

        // Binding for the Sheet Navigation Stack (Routes pushed on top of root)
        let sheetPathBinding = getPathBinding(
            backStack: state.sheet.backStack,
            onBack: { viewModel.onEvent(event: HomeEventOnBackClick.shared) }
        )

        NavigationStack(path: pathBinding) {
            HomeDestinationView(
                route: HomeRouteHome.shared,
                state: state,
                viewModel: viewModel
            )
                .navigationDestination(for: AnyRoute.self) { anyRoute in
                    HomeDestinationView(
                        route: anyRoute.base,
                        state: state,
                        viewModel: viewModel
                    )
                }
        }
        .sheet(item: sheetRootBinding) { wrapper in
            NavigationStack(path: sheetPathBinding) {
                // Sheet Root
                HomeDestinationView(
                    route: wrapper.base,
                    state: state,
                    viewModel: viewModel
                )
                .navigationTitle(state.sheet.title)
                .navigationBarTitleDisplayMode(.inline)
                .toolbar {
                    ToolbarItem(placement: .navigationBarLeading) {
                        Button("Close") {
                            viewModel.onEvent(event: HomeEventOnDismissSheet.shared)
                        }
                    }
                }
                .navigationDestination(for: AnyRoute.self) { anyRoute in
                    HomeDestinationView(
                        route: anyRoute.base,
                        state: state,
                        viewModel: viewModel
                    )
                        .navigationTitle(state.sheet.title)
                        .navigationBarTitleDisplayMode(.inline)
                }
            }
        }
        .overlay(alignment: .bottom) {
            if let message = toastMessage {
                // Simple Toast View Placeholder
                Text(message)
                    .padding()
                    .background(Color.black.opacity(0.8))
                    .foregroundColor(.white)
                    .cornerRadius(8)
                    .padding(.bottom, 20)
                    .onTapGesture {
                        onToastDismiss()
                    }
            }
        }
    }
}

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
                onRecentGamesViewAllClick: {
                    viewModel.onEvent(event: HomeEventOnRecentGamesViewAllClick.shared)
                },
                onResourceClick: { resourceId in
                    viewModel.onEvent(event: HomeEventOnResourceClick(resourceId: resourceId))
                },
                onResourcesViewAllClick: {
                    viewModel.onEvent(event: HomeEventOnResourcesViewAllClick.shared)
                }
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

        case is HomeRouteResourceDetails:
            ResourceDetailsView(
                resource: state.selectedResource
            )

        default:
            Text("Unknown Route")
        }
    }
}

#Preview {
    HomeView()
}
