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
    @State private var state: NavigationState = NavigationState.companion.Default
    @State private var sheetState: NavigationState = NavigationState.companion.Default

    var body: some View {
        let viewModel: NavigationViewModel = viewModelStoreOwner.viewModel(
            factory: NavigationViewModel.companion.createFactory()
        )

        // Sheet Binding
        let sheetBinding = getSheetRootBinding(
            sheetBackStack: sheetState.backStack,
            onDismiss: viewModel.dismissSheet
        )

        let pathBinding = getPathBinding(
            backStack: state.backStack,
            onBack: {
                viewModel.navigateBack()
            }
        )

        ZStack {
            if state.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .scaleEffect(1.5)
            } else {
                NavigationStack(path: pathBinding) {
                    Group {
                        if let rootRoute = state.backStack.first {
                            buildRootView(for: rootRoute)
                        } else {
                            // Fallback if backStack is unexpectedly empty when not loading.
                            // The shared ViewModel should ensure Welcome or Home is initialized.
                            Text("Loading Navigation...")
                        }
                    }
                    .navigationDestination(for: AnyRoute.self) { anyRoute in
                        buildRootView(for: anyRoute.base)
                    }
                }
            }
        }
        .environmentObject(viewModelStoreOwner)
        .sheet(item: sheetBinding) { sheetRoute in
            SheetView(route: sheetRoute.base)
                .presentationDetents([.medium, .large])
                .presentationDragIndicator(.visible)
        }
        .task {
            viewModel.navigationState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { self.state = $0 ?? .companion.Default }
            )
            viewModel.sheetState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { self.sheetState = $0 ?? .companion.Default }
            )
        }
    }

    @ViewBuilder
    private func buildRootView(for route: Route) -> some View {
        switch route {
        case is HomeRoute, is SearchRouteSearch, is SettingsRouteSettings:
            HomeRootView(route: route)
        case is AiRoute:
            AiAssistantView()
        case is OnboardingRoute:
            OnboardingView()
        default:
            Text("Unknown Global Route: \(route.path)")
        }
    }
}

struct SheetView: View {
    let route: Route

    var body: some View {
        if let homeRoute = route as? HomeRoute {
            HomeSheetView(route: homeRoute)
        } else {
            Text("Unknown Sheet: \(route.path)")
        }
    }
}

#Preview {
    MainNavigation()
}
