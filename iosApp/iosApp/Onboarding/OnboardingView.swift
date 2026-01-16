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
            onBack: { viewModel.onEvent(event: OnboardingEventOnNavigateBack.shared) },
            viewModel: viewModel
        )
        .environmentObject(viewModelStoreOwner)
        .task {
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? self.state }
            )
        }
        .task {
            viewModel.sideEffects.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { (sideEffect: OnboardingSideEffect) in
                    switch sideEffect {
                    case let sideEffect as OnboardingSideEffectNavigate:
                        onNavigateGlobal(sideEffect.route)
                    case let sideEffect as OnboardingSideEffectShowToast:
                        // TODO: Show toast (use a specific mechanism or alert)
                        print("Show toast: \(sideEffect.message)")
                    default:
                        break
                    }
                }
            )
        }
    }
}

private struct OnboardingContent: View {
    let state: OnboardingState
    let onBack: () -> Void
    let viewModel: OnboardingViewModel

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
                    )
                    .navigationDestination(for: AnyRoute.self) { anyRoute in
                        OnboardingDestinationView(
                            route: anyRoute.base,
                            state: state,
                            viewModel: viewModel,
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

    var body: some View {
        switch route {
        case is OnboardingRouteWelcome:
            WelcomeView(
                strings: state.strings,
                onStartClick: { viewModel.onEvent(event: OnboardingEventOnNavigate(route: OnboardingRouteRegistration.shared)) }
            )
        case is OnboardingRouteRegistration:
            RegistrationView(
                strings: state.strings,
                name: state.name,
                onNameChange: { viewModel.onEvent(event: OnboardingEventOnNameChange(name: $0)) },
                onRegisterClick: { name in
                    viewModel.onEvent(event: OnboardingEventOnRegisterClick(name: name))
                    viewModel.onEvent(event: OnboardingEventOnNavigate(route: OnboardingRouteTutorial.shared))
                },
                onRandomNameClick: { viewModel.onEvent(event: OnboardingEventOnGenerateRandomName.shared) },
                error: state.error,
                onErrorDismiss: { viewModel.onEvent(event: OnboardingEventOnClearError.shared) }
            )
        case is OnboardingRouteTutorial:
            TutorialView(
                strings: state.strings,
                onCompleteClick: { viewModel.onEvent(event: OnboardingEventOnCompleteOnboarding.shared) },
                )
        default:
            EmptyView()
        }
    }
}
