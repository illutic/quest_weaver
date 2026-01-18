import SwiftUI
import Shared

struct RecentGamesView: View {
    let strings: HomeStrings
    let games: [GameSession]
    let onGameClick: (String) -> Void

    var body: some View {
        ScrollView {
            LazyVGrid(columns: [GridItem(.adaptive(minimum: 340), spacing: Theme.Dimens.spacing2)], spacing: Theme.Dimens.spacing2) {
                ForEach(games, id: \.id) { game in
                    GameCard(
                        game: game,
                        strings: strings,
                        onClick: { onGameClick(game.id) }
                    )
                        .frame(maxWidth: .infinity)
                }
            }
            .padding(Theme.Dimens.spacing2)
            .frame(maxWidth: .infinity)
        }
        .navigationTitle(strings.recentGamesTitle)
        .navigationBarTitleDisplayMode(.inline)
    }
}
