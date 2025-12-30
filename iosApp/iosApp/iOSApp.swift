import SwiftUI
import Shared

@main
struct iOSApp: App {
    init() {
        DependencyHandler.shared.doInitKoin()
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
