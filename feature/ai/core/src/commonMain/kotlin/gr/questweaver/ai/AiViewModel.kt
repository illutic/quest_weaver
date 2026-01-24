package gr.questweaver.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
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
    private val navigationController: NavigationController by inject()

    private val _state = MutableStateFlow(AiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AiEffect>()
    val effect = _effect.asSharedFlow()

    init {
        _state.update {
            it.copy(
                quickActions =
                    listOf(
                        QuickAction(
                            "1",
                            "Generate Image",
                            "Generate an image of a dark fantasy castle"
                        ),
                        QuickAction(
                            "2",
                            "Create NPC",
                            "Create a level 5 goblin rogue NPC"
                        ),
                        QuickAction(
                            "3",
                            "Plot Hook",
                            "Give me a plot hook for a seaside village"
                        )
                    )
            )
        }
    }

    fun onEvent(event: AiEvent) {
        when (event) {
            is AiEvent.OnInputChanged -> {
                _state.update { it.copy(inputText = event.text) }
            }

            is AiEvent.OnSendClick -> {
                val input = state.value.inputText
                if (input.isNotBlank()) {
                    sendMessage(input)
                }
            }

            is AiEvent.OnQuickActionClick -> {
                sendMessage(event.action.query)
            }

            AiEvent.OnAttachClick -> {
                // TODO: Implement file picker
            }

            is AiEvent.OnRemoveAttachment -> {
                _state.update { it.copy(attachedFiles = it.attachedFiles - event.file) }
            }
        }
    }

    private fun sendMessage(text: String) {
        val userMessage =
            UiMessage(
                id = generateId(),
                text = text,
                isUser = true,
                timestamp = 0L // TODO: use clock
            )

        _state.update {
            it.copy(messages = it.messages + userMessage, inputText = "", isGenerating = true)
        }

        viewModelScope.launch {
            // Simulate network delay
            kotlinx.coroutines.delay(1500)

            val aiMessage =
                UiMessage(
                    id = generateId(),
                    text = "This is a simulated response to: \"$text\"",
                    isUser = false,
                    timestamp = 0L
                )

            _state.update { it.copy(messages = it.messages + aiMessage, isGenerating = false) }
        }
    }

    private fun generateId(): String = (state.value.messages.size + 1).toString()

    companion object {
        fun createFactory() = viewModelFactory { initializer { AiViewModel() } }
    }
}
