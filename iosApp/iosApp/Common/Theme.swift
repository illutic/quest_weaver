//
//  Theme.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

struct Theme {
    struct Colors {
        static var primary: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.primary.toUIColor() :
                    SharedTokens.shared.lightColorScheme.primary.toUIColor()
            })
        }

        static var onPrimary: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.onPrimary.toUIColor() :
                    SharedTokens.shared.lightColorScheme.onPrimary.toUIColor()
            })
        }

        static var secondary: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.secondary.toUIColor() :
                    SharedTokens.shared.lightColorScheme.secondary.toUIColor()
            })
        }

        static var onSecondary: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.onSecondary.toUIColor() :
                    SharedTokens.shared.lightColorScheme.onSecondary.toUIColor()
            })
        }

        static var background: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.background.toUIColor() :
                    SharedTokens.shared.lightColorScheme.background.toUIColor()
            })
        }

        static var onBackground: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.onBackground.toUIColor() :
                    SharedTokens.shared.lightColorScheme.onBackground.toUIColor()
            })
        }

        static var surface: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.surface.toUIColor() :
                    SharedTokens.shared.lightColorScheme.surface.toUIColor()
            })
        }

        // Add other needed colors similarly if used
    }

    struct Dimens {
        static let spacing1 = SharedTokens.shared.sizes.one.toCGFloat()
        static let spacing2 = SharedTokens.shared.sizes.two.toCGFloat()
        static let spacing4 = SharedTokens.shared.sizes.four.toCGFloat()
    }

    struct Layout {
        static let maxContentWidth: CGFloat = 600
    }
}

struct AdaptiveLayout: ViewModifier {
    @Environment(\.horizontalSizeClass) var sizeClass

    func body(content: Content) -> some View {
        if sizeClass == .regular {
            content
                .frame(maxWidth: Theme.Layout.maxContentWidth)
                .frame(maxWidth: .infinity, alignment: .center)
        } else {
            content
        }
    }
}

extension View {
    func adaptive() -> some View {
        modifier(AdaptiveLayout())
    }
}
