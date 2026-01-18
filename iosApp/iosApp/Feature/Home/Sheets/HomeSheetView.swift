//
// Created by George.Sigalas on 18/01/2026.
//

import Foundation
import Shared
import SwiftUI

struct HomeSheetView: View {
    let route: HomeRoute

    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: HomeState = HomeState.companion.Default

    var body: some View {
        let viewModel: HomeViewModel = viewModelStoreOwner.viewModel(
            factory: HomeViewModel.Companion.shared.createFactory()
        )

        NavigationStack {
            HomeSheetContentView(
                state: state,
                route: route,
                onSubmit: { gameTitle, gameType in
                    viewModel.onEvent(event: HomeEventOnSubmitCreateGame(title: gameTitle, type: gameType))
                },
                onCancel: {
                    viewModel.onEvent(event: HomeEventOnDismissSheet())
                }
            )
            .navigationTitle(getSheetTitle(route: route, state: state))
            .navigationBarTitleDisplayMode(.inline)
            .toolbar {
                ToolbarItem(placement: .navigationBarLeading) {
                    Button("Close") {
                        viewModel.onEvent(event: HomeEventOnDismissSheet())
                    }
                }
            }
        }
        .task {
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? HomeState.companion.Default }
            )
        }
    }

    private func getSheetTitle(route: HomeRoute, state: HomeState) -> String {
        if let r = route as? HomeRouteResourceDetails {
            return r.title
        }
        if route is HomeRouteCreateGame {
            return state.strings.createGameModalTitle
        }
        return ""
    }
}

struct HomeSheetContentView: View {
    let state: HomeState
    let route: HomeRoute
    let onSubmit: (String, GameType) -> Void
    let onCancel: () -> Void

    var body: some View {
        switch route {
        case is HomeRouteResourceDetails:
            ResourceDetailsView(
                resource: state.selectedResource
            )
        case is HomeRouteCreateGame:
            CreateGameView(
                strings: state.strings,
                onSubmit: onSubmit,
                onCancel: onCancel
            )
        default:
            EmptyView()
        }
    }
}
