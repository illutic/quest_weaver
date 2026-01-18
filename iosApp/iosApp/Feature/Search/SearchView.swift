//
//  SearchView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct SearchView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    var body: some View {
        let viewModel: SearchViewModel = viewModelStoreOwner.viewModel(
            factory: SearchViewModel.Companion.shared.createFactory()
        )

        VStack(spacing: Theme.Dimens.spacing4) {
            Spacer()

            Image(systemName: "magnifyingglass")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(Theme.Colors.primary)
                .padding()
                .background(Theme.Colors.primary.opacity(0.1))
                .clipShape(Circle())

            Text("Search")
                .font(Theme.Typography.displayLarge)
                .foregroundColor(Theme.Colors.onBackground)

            Text("Find games, players, and resources.")
                .font(Theme.Typography.bodyLarge)
                .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            Spacer()
        }
        .padding()
        .background(Theme.Colors.background)
        .adaptive()
        .navigationTitle("Search")
        .navigationBarTitleDisplayMode(.inline)
        .overlay(alignment: .bottom) {
            BottomBarView()
        }
    }
}
