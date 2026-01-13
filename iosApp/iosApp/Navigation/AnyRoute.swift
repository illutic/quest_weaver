//
//  AnyRoute.swift
//  iosApp
//
//  Created by George.Sigalas on 03/01/2026.
//

import Shared
import SwiftUI

struct AnyRoute: Hashable {
    let base: Route
    private let hasher: (inout Hasher) -> Void

    init(_ base: Route) {
        self.base = base
        // Attempt to hash using known concrete types first; fallback to type identity
        if let r = base as? any Hashable {
            self.hasher = { hasher in
                // Use the dynamic hash of the underlying value
                var anyHasher = Hasher()
                r.hash(into: &anyHasher)
                hasher.combine(anyHasher.finalize())
                hasher.combine(String(describing: type(of: base)))
            }
        } else {
            self.hasher = { hasher in
                hasher.combine(String(describing: type(of: base)))
            }
        }
    }

    static func == (lhs: AnyRoute, rhs: AnyRoute) -> Bool {
        // Prefer identity by equating type and, if possible, Equatable
        let lType = String(describing: type(of: lhs.base))
        let rType = String(describing: type(of: rhs.base))
        if lType != rType {
            return false
        }
        if let le = lhs.base as? any Equatable, let re = rhs.base as? any Equatable {
            // Best-effort: compare by String description as cross-casting isn't possible
            return String(describing: le) == String(describing: re)
        }
        return false
    }

    func hash(into hasher: inout Hasher) {
        self.hasher(&hasher)
    }
}

func toAnyRouteArray(from completeStack: [Route]) -> [AnyRoute] {
    return completeStack.map {
        AnyRoute($0)
    }
}

func getPathBinding(
    backStack: [Route],
    currentRoute: Route?,
    onBack: @escaping () -> Void,
    ) -> Binding<[AnyRoute]> {
    return Binding<[AnyRoute]>(
        get: {
            // backStack from VM already includes currentRoute
            if backStack.count <= 1 {
                return []
            } else {
                let dropped = Array(backStack.dropFirst())
                return toAnyRouteArray(from: dropped)
            }
        },
        set: { newPath in
            // Expected count is total stack size.
            // newPath.count is (stack size - 1) because root is implicit.
            // If newPath.count < (backStack.count - 1), user popped something.
            if newPath.count < (backStack.count - 1) {
                onBack()
            }
        }
    )

}
