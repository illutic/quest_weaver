package gr.questweaver.navigation

import org.koin.core.qualifier.named
import org.koin.dsl.module

val navigationModule =
    module(createdAtStart = true) {
        single(named(NavControllerType.App)) { NavigationController() }
        single(named(NavControllerType.Sheet)) { NavigationController() }
        factory { NavigationController() }
    }

enum class NavControllerType {
    App,
    Sheet
}