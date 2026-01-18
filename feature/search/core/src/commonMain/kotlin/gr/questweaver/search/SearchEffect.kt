package gr.questweaver.search

sealed interface SearchEffect {
    data class ShowError(val message: String) : SearchEffect
}
