//
//  RegistrationView.swift
//  iosApp
//
//  Created by Quest Weaver on 12/01/2026.
//

import SwiftUI
import Shared

struct RegistrationView: View {
    let strings: OnboardingStrings
    let name: String
    let onNameChange: (String) -> Void
    let onRegisterClick: (String) -> Void
    let onRandomNameClick: () -> Void
    let error: String?
    let onErrorDismiss: () -> Void

    @State private var isVisible = false

    var body: some View {
        VStack(spacing: 0) {
            // Fixed Header
            ProgressView(value: 0.66)
                .tint(Theme.Colors.primary)

            // Scrollable Content
            GeometryReader { geometry in
                ScrollView {
                    VStack(spacing: 0) {
                        Spacer()

                        VStack(spacing: 0) {
                            Text(strings.registrationTitle)
                                .font(.largeTitle)
                                .fontWeight(.bold)
                                .multilineTextAlignment(.center)
                                .foregroundColor(Theme.Colors.onBackground)

                            Text(strings.registrationSubtitle)
                                .font(.body)
                                .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                                .padding(.top, Theme.Dimens.spacing2)

                            Spacer().frame(height: Theme.Dimens.spacing4)

                            // Name Input
                            TextField(strings.registrationInputPlaceholder, text: Binding(
                                get: { name },
                                set: { onNameChange($0) }
                            ))
                                .padding()
                                .background(Theme.Colors.surface) // or surfaceVariant
                                .cornerRadius(Theme.Dimens.spacing2)
                                .overlay(
                                    RoundedRectangle(cornerRadius: Theme.Dimens.spacing2)
                                        .stroke(Theme.Colors.primary, lineWidth: 1)
                                )
                                .padding(.horizontal, Theme.Dimens.spacing4)

                            Spacer().frame(height: Theme.Dimens.spacing4)

                            // Info Text
                            HStack {
                                Image(systemName: "info.circle")
                                    .foregroundColor(Theme.Colors.primary)
                                Text(strings.registrationInfoText)
                            }
                            .font(.caption)
                            .foregroundColor(Theme.Colors.onBackground.opacity(0.6))
                        }
                        .opacity(isVisible ? 1.0 : 0.0)
                        .offset(y: isVisible ? 0 : 20)
                        .animation(.easeOut(duration: 0.5), value: isVisible)

                        Spacer()
                    }
                    .frame(maxWidth: .infinity, minHeight: geometry.size.height)
                    .padding(Theme.Dimens.spacing1)
                }
            }

            // Fixed Footer
            VStack {
                Button(action: {
                    onRegisterClick(name)
                }) {
                    Text(strings.registrationCreateButton)
                        .font(.headline)
                        .foregroundColor(Theme.Colors.onPrimary)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .background(name.isEmpty ? Color.gray : Theme.Colors.primary)
                        .cornerRadius(Theme.Dimens.spacing2)
                }
                .disabled(name.isEmpty)
                .padding(.horizontal, Theme.Dimens.spacing4)

                Button(action: {
                    onRandomNameClick()
                }) {
                    Text(strings.registrationRandomButton)
                        .font(.headline)
                        .foregroundColor(Theme.Colors.primary)
                        .frame(maxWidth: .infinity)
                        .padding()
                        .overlay(
                            RoundedRectangle(cornerRadius: Theme.Dimens.spacing2)
                                .stroke(Theme.Colors.primary, lineWidth: 1)
                        )
                }
                .padding(.horizontal, Theme.Dimens.spacing4)
                .padding(.top, Theme.Dimens.spacing2)
                .padding(.bottom, Theme.Dimens.spacing4)
            }
            .opacity(isVisible ? 1.0 : 0.0)
            .offset(y: isVisible ? 0 : 20)
            .animation(.easeOut(duration: 0.5), value: isVisible)
        }
        .background(Theme.Colors.background.ignoresSafeArea())
        .navigationBarHidden(true)
        .onAppear {
            isVisible = true
        }
        .alert(isPresented: Binding(
            get: { error != nil },
            set: { isPresenting in
                if !isPresenting {
                    onErrorDismiss()
                }
            }
        )) {
            Alert(
                title: Text("Error"),
                message: Text(error ?? ""),
                dismissButton: .default(Text("OK"))
            )
        }
    }
}
