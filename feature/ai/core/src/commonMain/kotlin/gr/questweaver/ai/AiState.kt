package gr.questweaver.ai

data class AiState(
    val messages: List<UiMessage> = emptyList(),
    val quickActions: List<QuickAction> = emptyList(),
    val inputText: String = "",
    val attachedFiles: List<AttachedFile> = emptyList(),
    val isGenerating: Boolean = false
) {
    companion object {
        val Default = AiState()
    }
}

data class UiMessage(
    val id: String,
    val text: String,
    val isUser: Boolean,
    val timestamp: Long
) {
    companion object {
        val Default = UiMessage(
            id = "",
            text = "",
            isUser = false,
            timestamp = 0L
        )
    }
}

data class QuickAction(
    val id: String,
    val label: String,
    val query: String // The prompt to send when clicked
) {
    companion object {
        val Default = QuickAction(id = "", label = "", query = "")
    }
}

data class AttachedFile(
    val uri: String,
    val name: String,
    val mimeType: String
) {
    companion object {
        val Default = AttachedFile(uri = "", name = "", mimeType = "")
    }
}
