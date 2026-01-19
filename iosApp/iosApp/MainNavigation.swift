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

    var body: some View {
        let viewModel: NavigationViewModel = viewModelStoreOwner.viewModel(
            factory: NavigationViewModel.companion.createFactory()
        )

        // Sheet Binding
        let sheetBinding = getSheetRootBinding(
            sheetBackStack: state.sheetBackStack,
            onDismiss: viewModel.dismissSheet,
            )

        ZStack {
            MainNavigationContent(
                navigationState: state,
                onBack: viewModel.navigateBack,
                onNavigate: viewModel.navigateTo
            )
                .environmentObject(viewModelStoreOwner)

            if state.isLoading {
                ProgressView()
                    .progressViewStyle(CircularProgressViewStyle())
                    .scaleEffect(1.5)
            }
        }
        .sheet(item: sheetBinding) { sheetRoute in
            SheetView(route: sheetRoute.base)
                .presentationDetents([.medium, .large])
                .presentationDragIndicator(.visible)
        }
        .task {
            viewModel.navigationState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? NavigationState.companion.Default }
            )
        }
    }
}

private struct MainNavigationContent: View {
    let navigationState: NavigationState
    let onBack: () -> Void
    let onNavigate: (Route) -> Void

    var body: some View {
        let pathBinding = getPathBinding(
            backStack: navigationState.visibleBackStack,
            onBack: onBack
        )

        NavigationStack(path: pathBinding) {
            if let root = navigationState.visibleBackStack.first {
                RouteView(
                    route: root,
                    navigationState: navigationState,
                    onNavigate: onNavigate
                )
                    .navigationDestination(for: AnyRoute.self) { anyRoute in
                        RouteView(
                            route: anyRoute.base,
                            navigationState: navigationState,
                            onNavigate: onNavigate
                        )
                    }
            } else {
                // Should not happen if app starts with a route
                Color.white
            }
        }
    }
}

struct RouteView: View {
    let route: Route
    let navigationState: NavigationState
    let onNavigate: (Route) -> Void

    var body: some View {
        switch route {
        case let r as OnboardingRoute:
            OnboardingView(route: r)
        case let r as HomeRoute:
            HomeView(route: r, navigationState: navigationState)
        case _ as AiRoute:
            AiAssistantView()
        case _ as SearchRoute:
            SearchView()
        case _ as SettingsRoute:
            SettingsView()
        default:
            Text("Unknown Route: \(route.path)")
        }
    }
}

struct SheetView: View {
    let route: SheetRoute

    var body: some View {
        // Simple wrapper for now, assuming Home sheets are the main ones.
        // In Android we have HomeSheetUi.
        // Here we can switch.
        if let homeRoute = route as? HomeRoute {
            // Need a HomeSheetView similar to HomeSheetUi
            HomeSheetView(route: homeRoute)
        } else {
            Text("Unknown Sheet: \(route.path)")
        }
    }
}

struct MainNavigation_Previews: PreviewProvider {
    static var previews: some View {
        MainNavigation()
    }
}
