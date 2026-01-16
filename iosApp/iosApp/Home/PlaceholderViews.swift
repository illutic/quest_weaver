//
//  PlaceholderViews.swift
//  iosApp
//
//  Created by Quest Weaver on 2026-01-16.
//

import SwiftUI

struct SearchPlaceholderView: View {
    var body: some View {
        ZStack {
            Color("Background")
                .ignoresSafeArea()
            Text("Search Feature Coming Soon")
                .font(.headline)
                .foregroundColor(.gray)
        }
    }
}

struct SettingsPlaceholderView: View {
    var body: some View {
        ZStack {
            Color("Background")
                .ignoresSafeArea()
            Text("Settings Feature Coming Soon")
                .font(.headline)
                .foregroundColor(.gray)
        }
    }
}
