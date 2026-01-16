//
//  TutorialView.swift
//  iosApp
//
//  Created by Quest Weaver on 12/01/2026.
//

import SwiftUI
import Shared

struct TutorialView: View {
    let strings: OnboardingStrings
    let onCompleteClick: () -> Void

    @State private var isVisible = false

    var body: some View {
        VStack(spacing: 0) {
            // Fixed Header
            ProgressView(value: 1.0)
                .tint(Theme.Colors.primary)

            // Scrollable Content
            GeometryReader { geometry in
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer()

                        Text(strings.tutorialTitle)
                            .font(.largeTitle)
                            .fontWeight(.bold)
                            .foregroundColor(Theme.Colors.onBackground)
                            .opacity(isVisible ? 1.0 : 0.0)
                            .offset(y: isVisible ? 0 : 20)
                            .animation(.easeOut(duration: 0.5), value: isVisible)

                        Spacer().frame(height: Theme.Dimens.spacing4)

                        VStack(alignment: .leading, spacing: Theme.Dimens.spacing4) {
                            TutorialItem(icon: "info.circle.fill", text: strings.tutorialItem1)
                                .opacity(isVisible ? 1.0 : 0.0)
                                .offset(x: isVisible ? 0 : -20)
                                .animation(.easeOut(duration: 0.5).delay(0.1), value: isVisible)

                            // Policy Item
                            HStack(alignment: .top) {
                                Image(systemName: "info.circle.fill")
                                    .resizable()
                                    .frame(width: 24, height: 24)
                                    .foregroundColor(Theme.Colors.primary)

                                policyText
                                    .font(.body)
                                    .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                            }
                            .opacity(isVisible ? 1.0 : 0.0)
                            .offset(x: isVisible ? 0 : -20)
                            .animation(.easeOut(duration: 0.5).delay(0.2), value: isVisible)

                            TutorialItem(icon: "star.fill", text: strings.tutorialItem3)
                                .opacity(isVisible ? 1.0 : 0.0)
                                .offset(x: isVisible ? 0 : -20)
                                .animation(.easeOut(duration: 0.5).delay(0.3), value: isVisible)

                            TutorialItem(icon: "star.fill", text: strings.tutorialItem4)
                                .opacity(isVisible ? 1.0 : 0.0)
                                .offset(x: isVisible ? 0 : -20)
                                .animation(.easeOut(duration: 0.5).delay(0.4), value: isVisible)
                        }
                        .padding(.horizontal, Theme.Dimens.spacing4)

                        Spacer()
                    }
                    .frame(maxWidth: .infinity, minHeight: geometry.size.height)
                    .padding(Theme.Dimens.spacing1)
                }
            }

            // Fixed Footer
            Button(action: {
                onCompleteClick()
            }) {
                Text(strings.tutorialButton)
                    .font(.headline)
                    .foregroundColor(Theme.Colors.onPrimary)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Theme.Colors.primary)
                    .cornerRadius(Theme.Dimens.spacing2)
            }
            .padding(.horizontal, Theme.Dimens.spacing4)
            .padding(.bottom, Theme.Dimens.spacing4)
            .opacity(isVisible ? 1.0 : 0.0)
            .offset(y: isVisible ? 0 : 40)
            .animation(.easeOut(duration: 0.5).delay(0.6), value: isVisible)
        }
        .navigationBarHidden(true)
        .onAppear {
            isVisible = true
        }
    }

    private var policyText: Text {
        let p1 = Text(strings.tutorialPolicyPart1)
        let privacy = Text(strings.tutorialPrivacyPolicy)
            .fontWeight(.bold)
            .foregroundColor(Theme.Colors.onBackground)
        let p2 = Text(strings.tutorialPolicyPart2)
        let terms = Text(strings.tutorialTermsOfService)
            .fontWeight(.bold)
            .foregroundColor(Theme.Colors.onBackground)

        return p1 + privacy + p2 + terms
    }
}

struct TutorialItem: View {
    let icon: String
    let text: String

    var body: some View {
        HStack {
            Image(systemName: icon)
                .resizable()
                .frame(width: 24, height: 24)
                .foregroundColor(Theme.Colors.primary)

            Text(text)
                .font(.body)
                .foregroundColor(Theme.Colors.onBackground)
        }
    }
}
