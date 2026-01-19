//
//  SettingsView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct SettingsView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()

    var body: some View {
        // We can create a basic view for now or use a ViewModel if one exists in shared code.
        // For now, mirroring SearchView pattern but simpler as SettingsScreen.kt is just text.
        
        VStack(spacing: Theme.Dimens.spacing4) {
            Spacer()

            Image(systemName: "gearshape")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(Theme.Colors.primary)
                .padding()
                .background(Theme.Colors.primary.opacity(0.1))
                .clipShape(Circle())

            Text("Settings")
                .font(Theme.Typography.displayLarge)
                .foregroundColor(Theme.Colors.onBackground)

            Text("Settings Feature Coming Soon")
                .font(Theme.Typography.bodyLarge)
                .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            Spacer()
        }
        .padding()
        .background(Theme.Colors.background)
        .adaptive()
        .navigationTitle("Settings")
        .navigationBarTitleDisplayMode(.inline)
        .overlay(alignment: .bottom) {
            BottomBarView()
        }
    }
}
