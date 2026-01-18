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
                        } else if let mode = state.mode as? BottomBarModeTextField {
                            TextFieldContent(
                                placeholder: mode.placeholder,
                                value: state.inputValue,
                                onEvent: viewModel.onEvent
                            )
                        }
                    }
                    .frame(height: 64)
                }
                .glass(shape: .capsule)
                .contentShape(.capsule)
                .padding(.horizontal, Theme.Dimens.spacing2)
                .frame(maxWidth: 300)
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
        HStack {
            TextField(placeholder, text: Binding(
                get: { value },
                set: { onEvent(BottomBarEventOnInputChanged(value: $0)) }
            ))
            .padding(.horizontal)
            
            Button(action: {
                onEvent(BottomBarEventOnSubmitClick.shared)
            }) {
                Image(systemName: "paperplane.fill")
                    .foregroundColor(Theme.Colors.primary)
            }
            .padding(.trailing)
        }
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
