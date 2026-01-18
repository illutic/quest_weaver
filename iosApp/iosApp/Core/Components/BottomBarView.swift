//
//  BottomBarView.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct BottomBarView: View {
    @StateObject private var viewModelStoreOwner = IosViewModelStoreOwner()
    @State private var state: BottomBarState = BottomBarState.Companion().Default

    var body: some View {
        let viewModel: BottomBarViewModel = viewModelStoreOwner.viewModel(
            factory: BottomBarViewModel.Companion.shared.createFactory()
        )

        VStack {
            if state.mode is BottomBarModeEmpty {
                EmptyView()
            } else {
                HStack(alignment: .bottom, spacing: 16) {
                    ZStack {
                        if state.mode is BottomBarModeStandard {
                            StandardContent(items: state.items, onEvent: viewModel.onEvent)
                                .frame(maxWidth: 300, minHeight: 64)
                                .glass(shape: .capsule)
                                .contentShape(Capsule())
                        } else if let mode = state.mode as? BottomBarModeTextField {
                            TextFieldContent(
                                placeholder: mode.placeholder,
                                value: state.inputValue,
                                onEvent: viewModel.onEvent
                            )
                                .frame(minHeight: 64)
                                .glass(shape: RoundedRectangle(cornerRadius: 16))
                                .contentShape(RoundedRectangle(cornerRadius: 16))
                        }
                    }
                }
                .padding(.horizontal, Theme.Dimens.spacing2)
            }
        }
        .task {
            viewModel.state.subscribe(
                scope: viewModel.viewModelScope,
                onError: { _ in },
                onNext: { (newState: BottomBarState?) in
                    if let s = newState {
                        withAnimation {
                            self.state = s
                        }
                    }
                }
            )
        }
    }
}

private struct StandardContent: View {
    let items: [BottomBarItem]
    let onEvent: (BottomBarEvent) -> Void

    var body: some View {
        HStack {
            ForEach(items, id: \.self) { item in
                Spacer()
                Button(action: {
                    onEvent(BottomBarEventOnItemClick(route: item.route))
                }) {
                    Image(systemName: item.icon.toSystemName())
                        .font(.system(size: 24))
                        .foregroundColor(item.selected ? Theme.Colors.primary : .gray)
                }
                Spacer()
            }
        }
    }
}

private struct TextFieldContent: View {
    let placeholder: String
    let value: String
    let onEvent: (BottomBarEvent) -> Void

    var body: some View {
        HStack(spacing: 8) {
            TextField(placeholder, text: Binding(
                get: { value },
                set: { onEvent(BottomBarEventOnInputChanged(value: $0)) }
            ), axis: .vertical)
                .padding(.vertical, 8)
                .padding(.horizontal, 16)
                .background(Theme.Colors.surface)
                .cornerRadius(20)
                .overlay(
                    RoundedRectangle(cornerRadius: 20)
                        .stroke(Theme.Colors.onBackground.opacity(0.1), lineWidth: 1)
                )

            Button(action: {
                onEvent(BottomBarEventOnSubmitClick.shared)
            }) {
                Image(systemName: "paperplane.fill")
                    .foregroundColor(Theme.Colors.onPrimary)
                    .padding(10)
                    .background(Theme.Colors.primary)
                    .clipShape(Circle())
            }
        }
        .padding(.horizontal, 8)
    }
}

extension BottomBarIcon {
    func toSystemName() -> String {
        switch self {
        case .home: return "house.fill"
        case .search: return "magnifyingglass"
        case .settings: return "gearshape.fill"
        case .back: return "arrow.left"
        default: return "questionmark"
        }
    }
}
