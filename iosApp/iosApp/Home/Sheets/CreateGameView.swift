import SwiftUI
import Shared

struct CreateGameView: View {
    let strings: HomeStrings
    let onSubmit: (String, GameType) -> Void
    let onCancel: () -> Void

    @State private var title: String = ""
    @State private var selectedType: GameType = .campaign

    var body: some View {
        VStack(spacing: 24) {
            VStack(alignment: .leading, spacing: 8) {
                TextField(strings.createGameInputLabel, text: Binding(
                    get: { title },
                    set: { title = $0 }
                ))
                    .padding()
                    .cornerRadius(Theme.Dimens.spacing2)
                    .overlay(
                        RoundedRectangle(cornerRadius: Theme.Dimens.spacing2)
                            .stroke(Theme.Colors.primary, lineWidth: 1)
                    )
                    .padding(.horizontal, Theme.Dimens.spacing4)
            }

            HStack(spacing: 16) {
                GameTypeButton(
                    label: strings.createGameCampaign,
                    isSelected: selectedType == GameType.campaign,
                    action: { selectedType = GameType.campaign }
                )

                GameTypeButton(
                    label: strings.createGameOneShot,
                    isSelected: selectedType == GameType.oneshot,
                    action: { selectedType = GameType.oneshot }
                )
            }

            Button(action: {
                onSubmit(title, selectedType)
            }) {
                Text(strings.startGameButton)
                    .font(.headline)
                    .foregroundColor(Theme.Colors.onPrimary)
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Theme.Colors.primary)
                    .cornerRadius(8)
            }
            .disabled(title.isEmpty)
            .opacity(title.isEmpty ? 0.6 : 1.0)

            Spacer()
        }
        .padding(.horizontal, 16)
        .background(Color(UIColor.systemBackground))
        .adaptive()
    }
}

struct GameTypeButton: View {
    let label: String
    let isSelected: Bool
    let action: () -> Void

    var body: some View {
        Button(action: action) {
            HStack {
                if isSelected {
                    Image(systemName: "checkmark")
                        .font(.caption)
                }
                Text(label)
            }
            .font(.subheadline)
            .foregroundColor(isSelected ? Theme.Colors.onPrimary : Theme.Colors.onBackground)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(
                RoundedRectangle(cornerRadius: 8)
                    .fill(isSelected ? Theme.Colors.primary : Color.clear)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 8)
                    .stroke(isSelected ? Color.clear : Theme.Colors.surface, lineWidth: 1)
            )
        }
    }
}
