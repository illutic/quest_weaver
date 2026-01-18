//
//  HomeView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct HomeView: View {
    let route: HomeRoute
    let navigationState: NavigationState
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: HomeState = HomeState.companion.Default

    var body: some View {
        let viewModel: HomeViewModel = viewModelStoreOwner.viewModel(
            factory: HomeViewModel.Companion.shared.createFactory()
        )

        HomeDestinationView(
            route: route,
            state: state,
            viewModel: viewModel
        )
        .overlay(alignment: .bottom) {
            BottomBarView()
        }
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
                onNext: { (effect: HomeSideEffect?) in }
            )
        }
    }
}
