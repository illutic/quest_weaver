//
//  AiAssistantView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct AiAssistantView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: AiState = AiState.companion.Default
    
    var body: some View {
        let viewModel: AiViewModel = viewModelStoreOwner.viewModel(
            factory: AiViewModel.Companion.shared.createFactory()
        )

        VStack(spacing: 0) {

            // Messages List
            ScrollViewReader { proxy in
                ScrollView {
                    LazyVStack(spacing: 16) {
                        if state.messages.isEmpty {
                            EmptyStateView()
                                .padding(.top, 40)
                        } else {
                            ForEach(state.messages, id: \.id) { message in
                                MessageBubbleView(message: message)
                            }
                        }
                    }
                    .padding()
                }
                .onChange(of: state.messages) { messages in
                    if let last = messages.last {
                        withAnimation {
                            proxy.scrollTo(last.id, anchor: .bottom)
                        }
                    }
                }
            }

            // Quick Actions & Input
            VStack(spacing: 0) {
                Divider()

                if !state.quickActions.isEmpty && state.messages.isEmpty {
                    ScrollView(.horizontal, showsIndicators: false) {
                        HStack(spacing: 8) {
                            ForEach(state.quickActions, id: \.id) { action in
                                QuickActionChip(action: action) {
                                    viewModel.onEvent(event: AiEventOnQuickActionClick(action: action))
                                }
                            }
                        }
                        .padding(.horizontal)
                        .padding(.vertical, 8)
                    }
                }

                InputAreaView(
                    inputText: Binding(
                        get: { state.inputText },
                        set: { viewModel.onEvent(event: AiEventOnInputChanged(text: $0)) }
                    ),
                    isGenerating: state.isGenerating,
                    onSend: { viewModel.onEvent(event: AiEventOnSendClick.shared) },
                    onAttach: { viewModel.onEvent(event: AiEventOnAttachClick.shared) }
                )
            }
        }
        .background(Theme.Colors.background)
        .navigationTitle("AI Assistant")
        .navigationBarTitleDisplayMode(.inline)
        .task {
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { state in self.state = state ?? AiState.companion.Default }
            )
        }
    }
}

private struct EmptyStateView: View {
    var body: some View {
        VStack(spacing: 16) {
            Image(systemName: "sparkles")
                .resizable()
                .scaledToFit()
                .frame(width: 64, height: 64)
                .foregroundColor(Theme.Colors.primary.opacity(0.6))

            Text("How can I help you today?")
                .font(Theme.Typography.titleMedium)
                .foregroundColor(Theme.Colors.onBackground)
        }
    }
}

private struct MessageBubbleView: View {
    let message: UiMessage

    var body: some View {
        HStack {
            if message.isUser {
                Spacer()
            }

            Text(message.text)
                .padding(12)
                .background(message.isUser ? Theme.Colors.primary : Theme.Colors.secondary)
                .foregroundColor(message.isUser ? Theme.Colors.onPrimary : Theme.Colors.onSecondary)
                .cornerRadius(16, corners: message.isUser ? [.topLeft, .topRight, .bottomLeft] : [.topLeft, .topRight, .bottomRight])
                .frame(maxWidth: 280, alignment: message.isUser ? .trailing : .leading)

            if !message.isUser {
                Spacer()
            }
        }
    }
}

private struct QuickActionChip: View {
    let action: QuickAction
    let onClick: () -> Void

    var body: some View {
        Button(role: nil, action: onClick) {
            HStack(spacing: 4) {
                Image(systemName: "sparkles")
                    .font(.caption)
                Text(action.label)
                    .font(.caption)
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 8)
            .background(Theme.Colors.secondary)
            .foregroundColor(Theme.Colors.onSecondary)
            .clipShape(Capsule())
        }
    }
}

private struct InputAreaView: View {
    @Binding var inputText: String
    let isGenerating: Bool
    let onSend: () -> Void
    let onAttach: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            Button(action: onAttach) {
                Image(systemName: "paperclip")
                    .foregroundColor(Theme.Colors.onBackground)
                    .padding(8)
            }

            TextField("Message...", text: $inputText)
                .padding(10)
                .background(Theme.Colors.background) // Or a slightly lighter shade
                .cornerRadius(20)
                .overlay(
                    RoundedRectangle(cornerRadius: 20)
                        .stroke(Theme.Colors.surface, lineWidth: 1)
                )

            if isGenerating {
                ProgressView()
                    .padding(8)
            } else {
                Button(action: onSend) {
                    Image(systemName: "arrow.up.circle.fill")
                        .resizable()
                        .frame(width: 32, height: 32)
                        .foregroundColor(!inputText.isEmpty ? Theme.Colors.primary : Theme.Colors.onSurface.opacity(0.3))
                }
                .disabled(inputText.isEmpty)
            }
        }
        .padding()
    }
}

// Extension for partial corner radius if needed, or just use standard
extension View {
    func cornerRadius(_ radius: CGFloat, corners: UIRectCorner) -> some View {
        clipShape(RoundedCorner(radius: radius, corners: corners))
    }
}

struct RoundedCorner: Shape {
    var radius: CGFloat = .infinity
    var corners: UIRectCorner = .allCorners

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(roundedRect: rect, byRoundingCorners: corners, cornerRadii: CGSize(width: radius, height: radius))
        return Path(path.cgPath)
    }
}

