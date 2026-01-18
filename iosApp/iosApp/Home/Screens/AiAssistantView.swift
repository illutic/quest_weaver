//
//  AiAssistantView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct AiAssistantView: View {
    let strings: HomeStrings
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @Environment(\.dismiss) var dismiss

    var body: some View {
        let viewModel: AiViewModel = viewModelStoreOwner.viewModel(
            factory: AiViewModel.Companion.shared.createFactory()
        )
        VStack(spacing: Theme.Dimens.spacing4) {
            Spacer()

            Image(systemName: "sparkles")
                .resizable()
                .scaledToFit()
                .frame(width: 80, height: 80)
                .foregroundColor(Theme.Colors.primary)
                .padding()
                .background(Theme.Colors.primary.opacity(0.1))
                .clipShape(Circle())

            Text(strings.aiAssistantTitle)
                .font(Theme.Typography.displayLarge)
                .foregroundColor(Theme.Colors.onBackground)

            Text("This feature is coming soon! Our goblins are working hard to train the dragons.")
                .font(Theme.Typography.bodyLarge)
                .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                .multilineTextAlignment(.center)
                .padding(.horizontal)

            Spacer()
        }
        .padding()
        .background(Theme.Colors.background)
        .adaptive()
        .navigationTitle(strings.aiAssistantTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}

#Preview {
    AiAssistantView(strings: HomeStrings.companion.Default)
}
