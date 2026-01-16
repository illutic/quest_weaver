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

        HomeTabScreen(
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

#Preview {
    HomeView()
}
