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
    @State private var errorMessage: String? = nil
    @State private var state: NavigationState? = nil

    var body: some View {
        let viewModel: NavigationViewModel = viewModelStoreOwner.viewModel(
            factory: NavigationViewModel.companion.createFactory(isUserRegisteredUseCase: get())
        )

        ZStack {
            if let currentRoute = state?.currentRoute {
                RouteView(route: currentRoute)
            }

            if state?.isLoading ?? false {
                Color.black.opacity(0.3).ignoresSafeArea()
                ProgressView()
            }
        }
        .task {
            viewModel.navigationState.subscribe(
                scope: viewModel.viewModelScope,
                onError: { error in self.errorMessage = error.description() },
                onNext: { state in self.state = state }
            )
        }
    }
}

struct RouteView: View {
    let route: Route?

    var body: some View {
        switch route {
        case is OnboardingRoute:
            OnboardingView()
        default:
            Text("Unknown Route")
        }
    }
}

struct MainNavigation_Previews: PreviewProvider {
    static var previews: some View {
        RouteView(route: nil)
    }
}
