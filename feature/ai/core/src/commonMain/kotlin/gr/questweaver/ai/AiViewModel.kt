package gr.questweaver.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.bottombar.BottomBarController
import gr.questweaver.bottombar.BottomBarMode
import gr.questweaver.navigation.NavigationController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AiViewModel : ViewModel(), KoinComponent {
    private val bottomBarController: BottomBarController by inject()
    private val navigationController: NavigationController by inject()

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            navigationController.state.collect {
                val mode = if (it.currentRoute is AiRoute.AiAssistant) {
                    BottomBarMode.TextField("Type your prompt...")
                } else {
                    BottomBarMode.Standard
                }
                bottomBarController.setMode(mode)
            }
        }
    }

    fun onEvent(event: AiEvent) {
        when (event) {
            is AiEvent.OnPromptChanged -> {
                _state.update { it.copy(prompt = event.prompt) }
            }

            AiEvent.OnGenerateClick -> {
                // TODO: Implement generation logic
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    // Simulate delay
                    // delay(2000)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            response = "I am a dragon, ask me anything!"
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun createFactory() = viewModelFactory {
            initializer { AiViewModel() }
        }
    }
}
