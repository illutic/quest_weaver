package gr.questweaver.ai

sealed interface AiEffect {
    data class ShowError(val message: String) : AiEffect
}
