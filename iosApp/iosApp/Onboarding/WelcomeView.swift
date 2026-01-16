//
//  WelcomeView.swift
//  iosApp
//
//  Created by Quest Weaver on 12/01/2026.
//

import SwiftUI
import Shared

struct WelcomeView: View {
    let strings: OnboardingStrings
    let onStartClick: () -> Void

    @State private var isVisible = false

    var body: some View {
        VStack(spacing: 0) {
            // Fixed Header
            ProgressView(value: 0.33)
                .tint(Theme.Colors.primary)

            // Scrollable Content
            GeometryReader { geometry in
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer()

                        Image("ic_logo")
                            .resizable()
                            .frame(width: 120, height: 120)
                            .scaleEffect(isVisible ? 1.0 : 0.5)
                            .opacity(isVisible ? 1.0 : 0.0)
                            .animation(.spring(response: 0.6, dampingFraction: 0.7), value: isVisible)

                        Spacer().frame(height: Theme.Dimens.spacing4)

                        VStack {
                            Text(strings.welcomeTitle)
                                .font(.largeTitle)
                                .fontWeight(.bold)
                                .multilineTextAlignment(.center)
                                .foregroundColor(Theme.Colors.onBackground)

                            Spacer().frame(height: Theme.Dimens.spacing4)

                            Text(strings.welcomeSubtitle)
                                .font(.body)
                                .multilineTextAlignment(.center)
                                .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                                .padding(.horizontal, Theme.Dimens.spacing4)
                        }
                        .opacity(isVisible ? 1.0 : 0.0)
                        .offset(y: isVisible ? 0 : 20)
                        .animation(.easeOut(duration: 0.6).delay(0.3), value: isVisible)

                        Spacer()
                    }
                    .frame(maxWidth: .infinity, minHeight: geometry.size.height)
                    .padding(Theme.Dimens.spacing1)
                }
            }

            // Fixed Footer
            Button(action: {
                onStartClick()
            }) {
                Text(strings.welcomeButton)
                    .font(.headline)
                    .foregroundColor(Theme.Colors.onPrimary)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Theme.Colors.primary)
                    .cornerRadius(Theme.Dimens.spacing2)
            }
            .padding(.horizontal, Theme.Dimens.spacing4)
            .padding(.bottom, Theme.Dimens.spacing2)
            .opacity(isVisible ? 1.0 : 0.0)
            .offset(y: isVisible ? 0 : 40)
            .animation(.easeOut(duration: 0.6).delay(0.6), value: isVisible)

        }
        .background(Theme.Colors.background.ignoresSafeArea())
        .navigationBarHidden(true)
        .onAppear {
            isVisible = true
        }
    }
}
