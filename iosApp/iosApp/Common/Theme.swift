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

        static var onSurface: SwiftUI.Color {
            Color(UIColor { trait in
                return trait.userInterfaceStyle == .dark ?
                    SharedTokens.shared.darkColorScheme.onSurface.toUIColor() :
                    SharedTokens.shared.lightColorScheme.onSurface.toUIColor()
            })
        }

        // Add other needed colors similarly if used
    }

    struct Dimens {
        static let spacing1 = SharedTokens.shared.sizes.one.toCGFloat()
        static let spacing2 = SharedTokens.shared.sizes.two.toCGFloat()
        static let spacing4 = SharedTokens.shared.sizes.four.toCGFloat()
        static let spacing6 = SharedTokens.shared.sizes.six.toCGFloat()
    }

    struct Layout {
        static let maxContentWidth: CGFloat = 600
    }

    struct Typography {
        static var displayLarge: Font {
            SharedTokens.shared.typography.displayLarge.toFont()
        }
        static var headlineMedium: Font {
            SharedTokens.shared.typography.headlineMedium.toFont()
        }
        static var titleMedium: Font {
            SharedTokens.shared.typography.titleMedium.toFont()
        }
        static var bodyLarge: Font {
            SharedTokens.shared.typography.bodyLarge.toFont()
        }
        static var labelMedium: Font {
            SharedTokens.shared.typography.labelMedium.toFont()
        }
    }
}

struct AdaptiveLayout: ViewModifier {
    @Environment(\.horizontalSizeClass) var sizeClass
    var alignment: Alignment = .center

    func body(content: Content) -> some View {
        if sizeClass == .regular {
            content
                .frame(maxWidth: Theme.Layout.maxContentWidth)
                .frame(maxWidth: .infinity, alignment: alignment)
        } else {
            content
        }
    }
}

extension View {
    func adaptive(alignment: Alignment = .center) -> some View {
        modifier(AdaptiveLayout(alignment: alignment))
    }
}

// MARK: - Glassmorphism

struct GlassMorphic<S: Shape>: ViewModifier {
    let shape: S

    init(shape: S) {
        self.shape = shape
    }

    func body(content: Content) -> some View {
        content
            .background(.ultraThinMaterial)
            .clipShape(shape)
            .shadow(color: Color.black.opacity(0.1), radius: 10, x: 0, y: 5)
            .overlay(
                shape
                    .stroke(
                        LinearGradient(
                            gradient: Gradient(colors: [
                                Color.white.opacity(0.6),
                                Color.white.opacity(0.1)
                            ]),
                            startPoint: .topLeading,
                            endPoint: .bottomTrailing
                        ),
                        lineWidth: 1
                    )
            )
    }
}

extension View {
    @ViewBuilder
    func glass<S: Shape>(shape: S = RoundedRectangle(cornerRadius: 16)) -> some View {
        if #available(iOS 26.0, *) {
            glassEffect(in: shape)
        } else {
            modifier(GlassMorphic(shape: shape))
        }
    }
}
