//
//  NavigationHelpers.swift
//  iosApp
//
//  Created by Quest Weaver.
//

import SwiftUI
import Shared

/// A wrapper to make Shared.Route Hashable for SwiftUI NavigationStack.
/// SwiftUI requires navigation paths to consist of Hashable elements. 
/// Since KMP `Route` interface doesn't inherit from Hashable (in Swift), we wrap it.
struct AnyRoute: Hashable {
    static func ==(lhs: AnyRoute, rhs: AnyRoute) -> Bool {
        ObjectIdentifier(lhs.base) == ObjectIdentifier(rhs.base)
    }

    let base: Route

    func hash(into hasher: inout Hasher) {
        hasher.combine(ObjectIdentifier(base))
    }
}

/// Creates a Binding<[AnyRoute]> for NavigationStack from KMP backStack.
///
/// This binding synchronizes the KMP `backStack` (source of truth) with SwiftUI's `NavigationStack`.
/// - `get`: Transforms the KMP backstack (removing the root) into a SwiftUI-compatible path.
/// - `set`: Detects when SwiftUI pops an item (new path is shorter) and triggers `onBack()`.
func getPathBinding(
    backStack: [Route],
    onBack: @escaping () -> Void
) -> Binding<[AnyRoute]> {
    Binding(
        get: {
            // Convert Route -> AnyRoute.
            // NavigationStack path represents *pushed* items, so we drop the root (first item).
            if backStack.count > 1 {
                return backStack.dropFirst().map {
                    AnyRoute(base: $0)
                }
            }
            return []
        },
        set: { newPath in
            // Detect Pop: If newPath is shorter than current effective path, user popped.
            // We blindly call onBack() for each popped item (though usually it's just 1).
            let currentStackSize = backStack.count
            let currentPathSize = max(0, currentStackSize - 1)

            if newPath.count < currentPathSize {
                onBack()
            }
        }
    )
}

/// A wrapper to make SheetRoute Identifiable for .sheet
struct AnySheetRoute: Identifiable {
    let base: SheetRoute

    var id: String {
        return base.id
    }
}

/// Creates a Binding<AnySheetRoute?> for the *root* of the sheet stack.
/// Used to control the presentation of the sheet itself.
func getSheetRootBinding(
    sheetBackStack: [SheetRoute],
    onDismiss: @escaping () -> Void
) -> Binding<AnySheetRoute?> {
    Binding(
        get: {
            if let first = sheetBackStack.first {
                return AnySheetRoute(base: first)
            }
            return nil
        },
        set: { newValue in
            if newValue == nil {
                onDismiss()
            }
        }
    )
}
