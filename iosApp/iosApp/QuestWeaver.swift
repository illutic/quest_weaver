import SwiftUI
import Shared

@main
struct QuestWeaver: App {
    init() {
        DependencyHandler.shared.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
