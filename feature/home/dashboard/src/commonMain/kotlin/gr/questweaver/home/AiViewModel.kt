package gr.questweaver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.questweaver.bottombar.BottomBarController
import gr.questweaver.bottombar.BottomBarMode
import gr.questweaver.navigation.NavigationController
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AiViewModel : ViewModel(), KoinComponent {
    private val bottomBarController: BottomBarController by inject()
    private val navigationController: NavigationController by inject()

    init {
        viewModelScope.launch {
            navigationController.state.collect {
                val mode = if (it.currentRoute == HomeRoute.AiAssistant) {
                    BottomBarMode.TextField("Type your prompt...")
                } else {
                    BottomBarMode.Standard
                }
                bottomBarController.setMode(mode)
            }
        }
    }
}