//
//  HomeTabScreen.swift
//  iosApp
//
//  Created by Quest Weaver on 2026-01-16.
//

import SwiftUI
import Shared

struct HomeTabScreen: View {
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

        // Binding for the Sheet Presentation
        let sheetRootBinding = getSheetRootBinding(
            sheetBackStack: state.sheet.backStack,
            onDismiss: { viewModel.onEvent(event: HomeEventOnDismissSheet.shared) }
        )

        // Binding for the Sheet Navigation Stack
        let sheetPathBinding = getPathBinding(
            backStack: state.sheet.backStack,
            onBack: { viewModel.onEvent(event: HomeEventOnBackClick.shared) }
        )

        // Tab Selection Binding
        let tabBinding = Binding<Int>(
            get: {
                if state.currentRoute is HomeRouteHome {
                    return 0
                }
                if state.currentRoute is HomeRouteSearch {
                    return 1
                }
                if state.currentRoute is HomeRouteSettings {
                    return 2
                }
                return 0
            },
            set: { index in
                let route: HomeRoute
                switch index {
                case 0: route = HomeRouteHome.shared
                case 1: route = HomeRouteSearch.shared
                case 2: route = HomeRouteSettings.shared
                default: route = HomeRouteHome.shared
                }
                viewModel.onEvent(event: HomeEventOnBottomNavClick(route: route))
            }
        )

        TabView(selection: tabBinding) {
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
            .tabItem {
                Label(state.strings.navDashboard, systemImage: "house.fill")
            }
            .tag(0)

            NavigationStack(path: pathBinding) {
                SearchPlaceholderView()
            }
            .tabItem {
                Label(state.strings.navSearch, systemImage: "magnifyingglass")
            }
            .tag(1)

            NavigationStack(path: pathBinding) {
                SettingsPlaceholderView()
            }
            .tabItem {
                Label(state.strings.navSettings, systemImage: "gearshape.fill")
            }
            .tag(2)
        }
        .accentColor(Theme.Colors.primary) // App Brand Color
        .sheet(item: sheetRootBinding) { wrapper in
            NavigationStack(path: sheetPathBinding) {
                HomeDestinationView(
                    route: wrapper.base,
                    state: state,
                    viewModel: viewModel
                )
                .navigationTitle(getSheetTitle(state: state))
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
                        .navigationTitle(getSheetTitle(state: state))
                        .navigationBarTitleDisplayMode(.inline)
                }
            }
            .presentationDetents([.medium, .large])
            .presentationDragIndicator(.visible)
            .overlay(alignment: .bottom) {
                if let message = toastMessage {
                    Text(message)
                        .padding()
                        .background(Color.black.opacity(0.8))
                        .foregroundColor(.white)
                        .cornerRadius(8)
                        .padding(.bottom, 60)
                        .onTapGesture {
                            onToastDismiss()
                        }
                }
            }
        }
    }

    private func getSheetTitle(state: HomeState) -> String {
        guard let route = state.sheet.backStack.last else {
            return ""
        }

        if let r = route as? HomeRouteResourceDetails {
            return r.title
        }
        if route is HomeRouteCreateGame {
            return state.strings.createGameModalTitle
        }
        return ""
    }
}
