package gr.questweaver.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.bottombar.BottomBarController
import gr.questweaver.navigation.NavigationController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class SearchViewModel : ViewModel(), KoinComponent {
    private val bottomBarController: BottomBarController by inject()
    private val navigationController: NavigationController by inject()

    private val _state = MutableStateFlow(SearchState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SearchEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.OnQueryChanged -> {
                _state.update { it.copy(query = event.query) }
            }

            SearchEvent.OnSearchClick -> {
                // TODO: Implement search logic
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    // Simulate search delay
                    // delay(1000)
                    _state.update {
                        it.copy(
                            isLoading = false,
                            results = listOf("Result 1", "Result 2") // Mock results
                        )
                    }
                }
            }
        }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { SearchViewModel() } }
    }
}
