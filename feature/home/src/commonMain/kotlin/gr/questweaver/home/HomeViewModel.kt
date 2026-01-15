package gr.questweaver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.navigation.Route
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

sealed interface HomeSideEffect {
    data class ShowToast(val message: String) : HomeSideEffect
}

class HomeViewModel(private val navigateToCallback: ((Route) -> Unit)? = null) :
    ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val strings = HomeStrings.load()
            _state.update {
                it.copy(
                    strings = strings,
                    recentGames =
                        kotlinx.collections.immutable.persistentListOf(
                            GameSession(
                                "1",
                                "The Lost Mine",
                                "Campaign",
                                5,
                                "DM Steve",
                                4,
                                true
                            ),
                            GameSession(
                                "2",
                                "Storm King's Thunder",
                                "One shot",
                                3,
                                "DM Sarah",
                                4,
                                false
                            )
                        ),
                    resources =
                        kotlinx.collections.immutable.persistentListOf(
                            Resource(
                                "1",
                                "Player's Handbook",
                                "Essential rules for D&D 5e",
                                ResourceType.Rulebook
                            ),
                            Resource(
                                "2",
                                "Character Sheet",
                                "Standard character sheet",
                                ResourceType.CharacterSheet
                            )
                        )
                )
            }
        }
    }

    fun handleNavigation(route: Route) {
        navigateToCallback?.invoke(route)
    }

    fun onJoinGameClick() {
        emitToast("Join Game Clicked!")
    }

    fun onCreateGameClick() {
        emitToast("Create Game Clicked!")
    }

    fun onGameClick(gameId: String) {
        emitToast("Game $gameId Clicked!")
    }

    fun navigateTo(route: Route) {
        _state.update { it.copy(backStack = it.backStack + route) }
    }

    fun navigateBack() {
        _state.update {
            if (it.backStack.size > 1) {
                it.copy(backStack = it.backStack.dropLast(1))
            } else {
                it
            }
        }
    }

    fun onRecentGamesViewAllClick() {
        navigateTo(HomeRoute.RecentGames)
    }

    fun onAiAssistantClick() {
        emitToast("AI Assistant Clicked!")
    }

    fun onResourceClick(resourceId: String) {
        emitToast("Resource $resourceId Clicked!")
    }

    fun onResourcesViewAllClick() {
        navigateTo(HomeRoute.ResourcesList)
    }

    private fun emitToast(message: String) {
        viewModelScope.launch { _sideEffects.send(HomeSideEffect.ShowToast(message)) }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { HomeViewModel() } }
    }
}
