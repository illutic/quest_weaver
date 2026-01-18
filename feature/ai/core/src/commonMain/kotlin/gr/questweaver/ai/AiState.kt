package gr.questweaver.ai

data class AiState(
    val prompt: String = "",
    val isLoading: Boolean = false,
    val response: String? = null
)
