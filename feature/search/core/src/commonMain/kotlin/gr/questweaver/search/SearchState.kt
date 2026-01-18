package gr.questweaver.search

data class SearchState(
    val query: String = "",
    val isLoading: Boolean = false,
    val results: List<String> = emptyList() // Placeholder for actual results
)
