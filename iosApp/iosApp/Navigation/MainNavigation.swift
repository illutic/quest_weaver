//
//  MainNavigation.swift
//  iosApp
//
//  Created by George.Sigalas on 03/01/2026.
//

import Foundation
import SwiftUI
import Shared

struct MainNavigation: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var errorMessage: String?
    @State private var state: NavigationState = NavigationState(backStack: [], currentRoute: nil, isLoading: false)

    var body: some View {
        let viewModel: NavigationViewModel = viewModelStoreOwner.viewModel(
            factory: NavigationViewModel.companion.createFactory(isUserRegisteredUseCase: get())
        )

        ZStack {
            MainNavigationContent(
                state: state,
                onBack: { viewModel.navigateBack() },
                onNavigate: { viewModel.navigateTo(route: $0) }
            )
                .environmentObject(viewModelStoreOwner)

            if state.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .scaleEffect(1.5)
            }
        }
        .task {
            viewModel.navigationState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? NavigationState(backStack: [], currentRoute: nil, isLoading: false) }
            )
        }
    }
}

private struct MainNavigationContent: View {
    let state: NavigationState
    let onBack: () -> Void
    let onNavigate: (Route) -> Void

    var body: some View {
        let pathBinding = getPathBinding(
            backStack: state.backStack,
            currentRoute: state.currentRoute,
            onBack: onBack
        )

        NavigationStack(path: pathBinding) {
            if let root = state.backStack.first {
                RouteView(route: root, onNavigate: onNavigate)
                    .navigationDestination(for: AnyRoute.self) { anyRoute in
                        RouteView(route: anyRoute.base, onNavigate: onNavigate)
                    }
            } else {
                Color.clear
            }
        }
    }
}

struct RouteView: View {
    let route: Route
    let onNavigate: (Route) -> Void

    var body: some View {
        switch route {
        case is OnboardingRouteGraph:
            OnboardingView(onNavigateGlobal: onNavigate)
        default:
            EmptyView()
        }
    }
}

struct MainNavigation_Previews: PreviewProvider {
    static var previews: some View {
        MainNavigation()
    }
}
