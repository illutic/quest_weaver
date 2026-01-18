//
//  HomeTabScreen.swift
//  iosApp
//
//  Created by Quest Weaver on 2026-01-16.
//

import SwiftUI
import Shared

struct HomeTabScreen: View {
    let route: HomeRoute
    let state: HomeState
    let navigationState: NavigationState
    let toastMessage: String?
    let viewModel: HomeViewModel
    let onToastDismiss: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            HomeDestinationView(
                route: route,
                state: state,
                viewModel: viewModel
            )
                .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .overlay(alignment: .bottom) {
            BottomBarView()
            if let message = toastMessage {
                Text(message)
                    .padding()
                    .background(Color.black.opacity(0.8))
                    .foregroundColor(.white)
                    .cornerRadius(8)
                    .onTapGesture {
                        onToastDismiss()
                    }
            }
        }
    }
}
