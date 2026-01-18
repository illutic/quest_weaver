package gr.questweaver.bottombar

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val bottomBarModule = module { singleOf(::BottomBarController) }
