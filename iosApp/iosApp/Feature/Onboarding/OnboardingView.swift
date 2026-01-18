//
//  OnboardingView.swift
//  iosApp
//
//  Created by George.Sigalas on 03/01/2026.
//

import Shared
import SwiftUI

struct OnboardingView: View {
    let route: OnboardingRoute

    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: OnboardingState = OnboardingState.companion.Default

    var body: some View {
        let viewModel: OnboardingViewModel = viewModelStoreOwner.viewModel(
            factory: OnboardingViewModel.companion.createFactory()
        )

        // Progress logic (simplified approximation)
        let progress: Float = {
            switch route {
            case is OnboardingRouteWelcome: return 0.33
            case is OnboardingRouteRegistration: return 0.66
            case is OnboardingRouteTutorial: return 1.0
            default: return 0.0
            }
        }()

        VStack {
            ProgressView(value: progress)
                .tint(Theme.Colors.primary)

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
                    onCompleteClick: { viewModel.onEvent(event: OnboardingEventOnCompleteOnboarding.shared) }
                )
            default:
                EmptyView()
            }
        }
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
