package gr.questweaver.ai

sealed interface AiEvent {
    data class OnInputChanged(val text: String) : AiEvent
    data object OnSendClick : AiEvent
    data class OnQuickActionClick(val action: QuickAction) : AiEvent
    data object OnAttachClick : AiEvent
    data class OnRemoveAttachment(val file: AttachedFile) : AiEvent
}
