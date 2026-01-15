//
//  HomeComponents.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct WelcomeSection: View {
    let strings: HomeStrings

    var body: some View {
        VStack(alignment: .leading, spacing: Theme.Dimens.spacing1) {
            Text(strings.welcomeTitle)
                .font(.headline)
                .foregroundColor(Theme.Colors.onBackground)

            Text(strings.welcomeSubtitle)
                .font(.title2)
                .fontWeight(.bold)
                .foregroundColor(Theme.Colors.primary)
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal, Theme.Dimens.spacing2)
    }
}

struct RecentGamesSection: View {
    let strings: HomeStrings
    let games: [GameSession]
    let onGameClick: (String) -> Void
    let onViewAllClick: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: Theme.Dimens.spacing2) {
            SectionHeader(title: strings.recentGamesTitle, actionText: strings.recentGamesViewAll, onAction: onViewAllClick)

            if games.isEmpty {
                Text(strings.recentGamesEmpty)
                    .font(.body)
                    .foregroundColor(Theme.Colors.onBackground.opacity(0.6))
                    .padding(.horizontal, Theme.Dimens.spacing2)
            } else {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack(spacing: Theme.Dimens.spacing2) {
                        ForEach(games, id: \.id) { game in
                            GameCard(game: game, strings: strings, onClick: { onGameClick(game.id) })
                                .frame(width: 260) // Fixed width for horizontal scroll
                        }
                    }
                    .padding(.horizontal, Theme.Dimens.spacing2)
                    .padding(.vertical, 20) // Avoid shadow clipping
                }
            }
        }
    }
}

// MARK: - Generic Components

struct ButtonScaleButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .scaleEffect(configuration.isPressed ? 0.95 : 1.0)
            .animation(.spring(response: 0.3, dampingFraction: 0.6), value: configuration.isPressed)
    }
}

struct GameCard: View {
    let game: GameSession
    let strings: HomeStrings
    let onClick: () -> Void

    var body: some View {
        Button(role: nil, action: onClick) {
            VStack(alignment: .leading, spacing: 0) { // Spacing 0 to manage padding manually
                // Header: Icon + Title/Details + Status
                HStack(alignment: .top, spacing: Theme.Dimens.spacing2) {
                    // Game Icon Placeholder
                    ZStack {
                        LinearGradient(colors: [Theme.Colors.primary.opacity(0.1), Theme.Colors.secondary.opacity(0.1)], startPoint: .topLeading, endPoint: .bottomTrailing)

                        Image(systemName: "gamecontroller.fill")
                            .resizable()
                            .scaledToFit()
                            .padding(12)
                            .foregroundColor(Theme.Colors.secondary)
                    }
                    .frame(width: 48, height: 48)
                    .clipShape(RoundedRectangle(cornerRadius: 8))

                    VStack(alignment: .leading, spacing: 4) {
                        Text(game.title)
                            .font(.headline)
                            .foregroundColor(Theme.Colors.onBackground)
                            .lineLimit(1)

                        Text("\(game.type) • Level \(game.level)")
                            .font(.caption)
                            .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                    }

                    Spacer()

                    StatusBadge(isStats: false, text: game.isLive ? strings.gameLive : strings.gameOffline, isLive: game.isLive)
                }
                .padding(Theme.Dimens.spacing2)

                // Player Count (Bottom)
                Text(strings.gamePlayers.replacingOccurrences(of: "%s", with: String(game.players)))
                    .font(.caption)
                    .fontWeight(.medium)
                    .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                    .padding(.horizontal, Theme.Dimens.spacing2)
                    .padding(.bottom, Theme.Dimens.spacing2)
            }
            .glassCard()
        }
        .buttonStyle(ButtonScaleButtonStyle())
    }
}

struct QuickActionsSection: View {
    let strings: HomeStrings
    let onCreateGameClick: () -> Void
    let onJoinGameClick: () -> Void

    // Gradients matching Android
    // 0xFF7F52FF -> 0xFFC852FF
    let createGradient = LinearGradient(
        gradient: Gradient(colors: [
            Color(red: 0x7F / 255.0, green: 0x52 / 255.0, blue: 0xFF / 255.0),
            Color(red: 0xC8 / 255.0, green: 0x52 / 255.0, blue: 0xFF / 255.0)
        ]),
        startPoint: .leading,
        endPoint: .trailing
    )

    // 0xFFC852FF -> 0xFFFF52C8
    let joinGradient = LinearGradient(
        gradient: Gradient(colors: [
            Color(red: 0xC8 / 255.0, green: 0x52 / 255.0, blue: 0xFF / 255.0),
            Color(red: 0xFF / 255.0, green: 0x52 / 255.0, blue: 0xC8 / 255.0)
        ]),
        startPoint: .leading,
        endPoint: .trailing
    )

    var body: some View {
        VStack(alignment: .leading, spacing: Theme.Dimens.spacing2) {
            Text(strings.quickActionsTitle)
                .font(.title3)
                .fontWeight(.bold)
                .foregroundColor(Theme.Colors.onBackground)
                .padding(.horizontal, Theme.Dimens.spacing2)

            HStack(spacing: Theme.Dimens.spacing2) {
                QuickActionButton(
                    title: strings.createGameButton,
                    icon: "plus",
                    gradient: createGradient,
                    action: onCreateGameClick
                )

                QuickActionButton(
                    title: strings.joinGameButton,
                    icon: "person.3.fill",
                    gradient: joinGradient,
                    action: onJoinGameClick
                )
            }
            .padding(.horizontal, Theme.Dimens.spacing2)
        }
    }
}

struct QuickActionButton: View {
    let title: String
    let icon: String
    let gradient: LinearGradient
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            VStack {
                Image(systemName: icon)
                    .font(.title3)
                    .padding(.bottom, 8)
                Text(title)
                    .fontWeight(.bold)
                    .font(.subheadline)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 100)
            .background(gradient)
            .foregroundColor(.white)
            .cornerRadius(16)
            .shadow(color: Color.black.opacity(0.15), radius: 8, x: 0, y: 4)
            .overlay(
                RoundedRectangle(cornerRadius: 16)
                    .stroke(Color.white.opacity(0.2), lineWidth: 1)
            )
        }
        .buttonStyle(ButtonScaleButtonStyle())
    }
}

struct ResourcesSection: View {
    let strings: HomeStrings
    let resources: [Resource]
    let onAiAssistantClick: () -> Void
    let onResourceClick: (String) -> Void
    let onViewAllClick: () -> Void

    var body: some View {
        VStack(alignment: .leading, spacing: Theme.Dimens.spacing2) {
            SectionHeader(title: strings.usefulResourcesTitle, actionText: strings.usefulResourcesViewAll, onAction: onViewAllClick)

            VStack(spacing: Theme.Dimens.spacing2) {
                AiAssistantCard(strings: strings, onClick: onAiAssistantClick)

                ForEach(resources, id: \.id) { resource in
                    ResourceCard(resource: resource, onClick: { onResourceClick(resource.id) })
                }
            }
            .padding(.horizontal, Theme.Dimens.spacing2)
        }
    }
}

struct AiAssistantCard: View {
    let strings: HomeStrings
    let onClick: () -> Void

    var body: some View {
        Button(action: onClick) {
            HStack {
                Image(systemName: "sparkles")
                    .resizable()
                    .frame(width: 24, height: 24)
                    .foregroundColor(.white)
                    .padding(10)
                    .background(LinearGradient(colors: [.purple, .blue], startPoint: .topLeading, endPoint: .bottomTrailing))
                    .clipShape(Circle())
                    .shadow(color: .purple.opacity(0.4), radius: 5, x: 0, y: 0)

                VStack(alignment: .leading) {
                    Text(strings.aiAssistantTitle)
                        .font(.headline)
                        .foregroundColor(Theme.Colors.onBackground)
                    Text(strings.aiAssistantDescription)
                        .font(.subheadline)
                        .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                        .lineLimit(1)
                }
                Spacer()
                Image(systemName: "chevron.right")
                    .foregroundColor(Theme.Colors.onBackground.opacity(0.5))
            }
            .padding()
            .glassCard()
        }
        .buttonStyle(ButtonScaleButtonStyle())
    }
}

struct ResourceCard: View {
    let resource: Resource
    let onClick: () -> Void

    var body: some View {
        Button(role: nil, action: onClick) {
            VStack(alignment: .leading, spacing: 0) {
                // Large Image Placeholder
                ZStack {
                    Color.gray.opacity(0.2)

                    Image(systemName: "doc.text.fill")
                        .resizable()
                        .scaledToFit()
                        .frame(width: 48, height: 48)
                        .foregroundColor(Theme.Colors.onBackground.opacity(0.5))
                }
                .aspectRatio(1.8, contentMode: .fit)
                .frame(maxWidth: .infinity)
                .clipped()

                VStack(alignment: .leading, spacing: Theme.Dimens.spacing1) {
                    Text(resource.title)
                        .font(.headline) // titleMedium
                        .fontWeight(.bold)
                        .foregroundColor(Theme.Colors.onBackground)

                    Text(resource.description_)
                        .font(.caption) // bodySmall
                        .foregroundColor(Theme.Colors.onBackground.opacity(0.7))
                        .lineLimit(2)
                        .multilineTextAlignment(.leading)
                }
                .padding(Theme.Dimens.spacing2)
            }
            .glassCard()
        }
        .buttonStyle(ButtonScaleButtonStyle())
    }
}

// MARK: - Generic Components

struct SectionHeader: View {
    let title: String
    let actionText: String
    let onAction: () -> Void

    var body: some View {
        HStack {
            Text(title)
                .font(.title3)
                .fontWeight(.bold)
                .foregroundColor(Theme.Colors.onBackground)
            Spacer()
            Button(action: onAction) {
                Text(actionText)
                    .font(.subheadline)
                    .foregroundColor(Theme.Colors.primary)
            }
        }
        .padding(.horizontal, Theme.Dimens.spacing2)
    }
}

struct StatusBadge: View {
    let isStats: Bool
    let text: String
    let isLive: Bool

    @State private var isPulsing = false

    var body: some View {
        Text(text)
            .font(.caption2)
            .fontWeight(.bold)
            .padding(.horizontal, 6)
            .padding(.vertical, 2)
            .background(isLive ? Color.green.opacity(0.2) : Color.gray.opacity(0.2))
            .foregroundColor(isLive ? Color.green : Color.gray)
            .cornerRadius(4)
            .opacity(isLive && isPulsing ? 0.6 : 1.0)
            .onAppear {
                if isLive {
                    withAnimation(Animation.easeInOut(duration: 1.0).repeatForever(autoreverses: true)) {
                        isPulsing = true
                    }
                }
            }
    }
}

#Preview {
    ScrollView {
        VStack(spacing: 20) {
            WelcomeSection(strings: HomeStrings.companion.Empty)

            QuickActionsSection(
                strings: HomeStrings.companion.Empty,
                onCreateGameClick: {},
                onJoinGameClick: {}
            )

            RecentGamesSection(
                strings: HomeStrings.companion.Empty,
                games: [],
                onGameClick: { _ in },
                onViewAllClick: {}
            )

            ResourcesSection(
                strings: HomeStrings.companion.Empty,
                resources: [],
                onAiAssistantClick: {},
                onResourceClick: { _ in },
                onViewAllClick: {}
            )
        }
        .padding()
    }
}
