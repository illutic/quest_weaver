//
//  OnboardingView.swift
//  iosApp
//
//  Created by George.Sigalas on 03/01/2026.
//

import Shared
import SwiftUI

struct OnboardingView: View {
    let onNavigateGlobal: (Route) -> Void

    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: OnboardingState = OnboardingState.companion.Default

    var body: some View {
        let viewModel: OnboardingViewModel = viewModelStoreOwner.viewModel(
            factory: OnboardingViewModel.companion.createFactory()
        )

        OnboardingContent(
            state: state,
            onBack: { viewModel.navigateBack() },
            viewModel: viewModel,
            onNavigateGlobal: onNavigateGlobal
        )
            .environmentObject(viewModelStoreOwner)
            .task {
                viewModel.state.subscribe(
                    scope: viewModel.viewModelScope,
                    onError: { _ in },
                    onNext: { state in self.state = state ?? self.state }
                )
            }
    }
}

private struct OnboardingContent: View {
    let state: OnboardingState
    let onBack: () -> Void
    let viewModel: OnboardingViewModel
    let onNavigateGlobal: (Route) -> Void

    var body: some View {
        let pathBinding = getPathBinding(
            backStack: state.backStack,
            onBack: onBack
        )

        NavigationStack(path: pathBinding) {
            if let root = state.backStack.first {
                OnboardingDestinationView(
                    route: root,
                    state: state,
                    viewModel: viewModel,
                    onNavigateGlobal: onNavigateGlobal
                )
                    .navigationDestination(for: AnyRoute.self) { anyRoute in
                        OnboardingDestinationView(
                            route: anyRoute.base,
                            state: state,
                            viewModel: viewModel,
                            onNavigateGlobal: onNavigateGlobal
                        )
                    }
            }
        }
    }
}

private struct OnboardingDestinationView: View {
    let route: Route
    let state: OnboardingState
    let viewModel: OnboardingViewModel
    let onNavigateGlobal: (Route) -> Void

    var body: some View {
        switch route {
        case is OnboardingRouteWelcome:
            WelcomeView(
                strings: state.strings,
                drawables: state.drawables,
                onStartClick: { viewModel.navigateTo(route: OnboardingRouteRegistration.shared) }
            )
        case is OnboardingRouteRegistration:
            RegistrationView(
                strings: state.strings,
                name: state.name,
                onNameChange: { viewModel.onNameChange(name: $0) },
                onRegisterClick: { name in
                    viewModel.registerUser(name: name)
                    viewModel.navigateTo(route: OnboardingRouteTutorial.shared)
                },
                onRandomNameClick: { viewModel.generateRandomName() },
                error: state.error,
                onErrorDismiss: { viewModel.clearError() }
            )
        case is OnboardingRouteTutorial:
            TutorialView(
                strings: state.strings,
                onCompleteClick: { onNavigateGlobal(OnboardingRouteGraph.shared) }
            )
        default:
            EmptyView()
        }
    }
}
