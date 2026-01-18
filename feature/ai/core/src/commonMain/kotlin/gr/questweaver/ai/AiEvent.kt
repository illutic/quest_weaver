package gr.questweaver.ai

sealed interface AiEvent {
    data class OnPromptChanged(val prompt: String) : AiEvent
    data object OnGenerateClick : AiEvent
}
