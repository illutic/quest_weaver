import SwiftUI
import Shared

struct RecentGamesView: View {
    let strings: HomeStrings
    let games: [GameSession]
    let onGameClick: (String) -> Void

    var body: some View {
        ScrollView {
            VStack(spacing: Theme.Dimens.spacing2) {
                ForEach(games, id: \.id) { game in
                    GameCard(
                        game: game,
                        strings: strings,
                        onClick: { onGameClick(game.id) }
                    )
                }
            }
            .padding(Theme.Dimens.spacing2)
        }
        .background(Theme.Colors.background.ignoresSafeArea())
        .navigationTitle(strings.recentGamesTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}
