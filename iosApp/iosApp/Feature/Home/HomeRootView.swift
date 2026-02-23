//
//  HomeRootView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

enum MainTab: Int, CaseIterable {
    case home = 0
    case search = 1
    case settings = 2
    
    var title: String {
        switch self {
        case .home: return "Home"
        case .search: return "Search"
        case .settings: return "Settings"
        }
    }
    
    var icon: String {
        switch self {
        case .home: return "house.fill"
        case .search: return "magnifyingglass"
        case .settings: return "gearshape.fill"
        }
    }
    
    var route: Route {
        switch self {
        case .home: return HomeRouteHome.shared
        case .search: return SearchRouteSearch.shared
        case .settings: return SettingsRouteSettings.shared
        }
    }
    
    init?(route: Route) {
        if route is HomeRoute {
            self = .home
        } else if route is SearchRouteSearch {
            self = .search
        } else if route is SettingsRouteSettings {
            self = .settings
        } else {
            return nil
        }
    }
}

struct HomeRootView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var selectedTab: MainTab

    init(route: Route) {
        _selectedTab = State(initialValue: MainTab(route: route) ?? .home)
    }

    var body: some View {
        let viewModel: HomeNavigationViewModel = viewModelStoreOwner.viewModel(
            factory: HomeNavigationViewModel.Companion.shared.createFactory()
        )

        TabView(selection: $selectedTab) {
            // Home Tab
            HomeTabContent()
            .tabItem {
                Label(MainTab.home.title, systemImage: MainTab.home.icon)
            }
            .tag(MainTab.home)
            
            // Search Tab
            SearchView()
                .tabItem {
                    Label(MainTab.search.title, systemImage: MainTab.search.icon)
                }
                .tag(MainTab.search)
            
            // Settings Tab
            SettingsView()
                .tabItem {
                    Label(MainTab.settings.title, systemImage: MainTab.settings.icon)
                }
                .tag(MainTab.settings)
        }
        .environmentObject(viewModelStoreOwner)
        .onChange(of: selectedTab) { _, newTab in
            viewModel.onNavigate(route: newTab.route)
        }
    }
}

// Home Tab with NavigationStack for detail screens
struct HomeTabContent: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var homeState: HomeState = HomeState.companion.Default
    @State private var homeNavigationState: NavigationState = NavigationState.companion.Default

    var body: some View {
        let viewModel: HomeDashboardViewModel = viewModelStoreOwner.viewModel(
            factory: HomeDashboardViewModel.Companion.shared.createFactory()
        )

        let pathBinding = getPathBinding(
            backStack: homeNavigationState.backStack,
            onBack: {
                viewModel.onEvent(event: HomeEventOnBackClick.shared)
            }
        )

        let homeViewBuilder = { (targetRoute: Route?) in
            HomeView(
                route: targetRoute,
                state: homeState,
                onGameClick: { viewModel.onEvent(event: HomeEventOnGameClick(gameId: $0)) },
                onGameViewAllClick: { viewModel.onEvent(event: HomeEventOnRecentGamesViewAllClick.shared) },
                onCreateGameClick: { viewModel.onEvent(event: HomeEventOnCreateGameClick.shared) },
                onJoinGameClick: { viewModel.onEvent(event: HomeEventOnJoinGameClick.shared) },
                onAiClick: { viewModel.onEvent(event: HomeEventOnAiAssistantClick.shared) },
                onResourceClick: { viewModel.onEvent(event: HomeEventOnResourceClick(resourceId: $0)) },
                onResourceViewAllClick: { viewModel.onEvent(event: HomeEventOnResourcesViewAllClick.shared) }
            )
        }

        NavigationStack(path: pathBinding) {
            homeViewBuilder(homeNavigationState.backStack.first)
                .navigationDestination(for: AnyRoute.self) { anyRoute in
                    homeViewBuilder(anyRoute.base)
                }
        }
        .task {
            // Subscribe to the home-specific navigation state
            viewModel.homeNavigationState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { homeNavigationState = $0 ?? .companion.Default }
            )
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { homeState = $0 ?? .companion.Default }
            )
        }
    }
}
