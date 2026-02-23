package gr.questweaver.search

sealed interface SearchEvent {
    data class OnQueryChanged(val query: String) : SearchEvent
    data object OnSearchClick : SearchEvent
}
