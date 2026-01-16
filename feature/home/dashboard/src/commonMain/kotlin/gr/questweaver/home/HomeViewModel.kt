package gr.questweaver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.navigation.Route
import gr.questweaver.navigation.SheetRoute
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class HomeViewModel : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val strings = loadHomeStrings()
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
                                ResourceType.Rulebook,
                                "https://example.com/phb.jpg"
                            ),
                            Resource(
                                "2",
                                "Character Sheet",
                                "Standard character sheet",
                                ResourceType.CharacterSheet,
                                "https://example.com/charsheet.jpg"
                            )
                        )
                )
            }
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnJoinGameClick -> emitToast("Join Game Clicked!")
            is HomeEvent.OnCreateGameClick -> navigateTo(HomeRoute.CreateGame)
            is HomeEvent.OnGameClick -> emitToast("Game ${event.gameId} Clicked!")
            is HomeEvent.OnRecentGamesViewAllClick -> navigateTo(HomeRoute.RecentGames)
            is HomeEvent.OnAiAssistantClick -> emitToast("AI Assistant Clicked!")
            is HomeEvent.OnResourceClick -> {
                val resource = state.value.resources.find { it.id == event.resourceId }
                if (resource != null) {
                    _state.update { it.copy(selectedResource = resource) }
                    val title = resource.title
                    navigateTo(HomeRoute.ResourceDetails(event.resourceId, title))
                }
            }
            is HomeEvent.OnResourcesViewAllClick -> navigateTo(HomeRoute.ResourcesList)
            is HomeEvent.OnBottomNavClick -> switchTab(event.route)
            is HomeEvent.OnBackClick -> navigateBack()
            is HomeEvent.OnDismissSheet -> dismissSheet()
            is HomeEvent.OnSubmitCreateGame -> createGame(event.title, event.type)
        }
    }

    private fun createGame(title: String, type: GameType) {
        // Mock game creation
        val newGame =
            GameSession(
                id = (state.value.recentGames.size + 1).toString(),
                title = title,
                type = type.displayName,
                level = 1,
                master = "You",
                players = 4,
                isLive = false
            )
        _state.update {
            val updatedGames = it.recentGames.toPersistentList().add(0, newGame)
            it.copy(recentGames = updatedGames, sheet = it.sheet.copy(backStack = emptyList()))
        }
    }

    private fun switchTab(route: Route) {
        _state.update { it.copy(backStack = listOf(route)) }
    }

    private fun navigateTo(route: Route) {
        _state.update {
            if (route is SheetRoute) {
                it.copy(sheet = it.sheet.copy(backStack = it.sheet.backStack + route))
            } else {
                it.copy(backStack = it.backStack + route)
            }
        }
    }

    private fun navigateBack() {
        _state.update {
            if (it.sheet.backStack.isNotEmpty()) {
                val newSheetStack = it.sheet.backStack.dropLast(1)
                it.copy(sheet = it.sheet.copy(backStack = newSheetStack))
            } else if (it.backStack.size > 1) {
                it.copy(backStack = it.backStack.dropLast(1))
            } else {
                it
            }
        }
    }

    private fun dismissSheet() {
        _state.update { it.copy(sheet = it.sheet.copy(backStack = emptyList())) }
    }

    private fun emitToast(message: String) {
        viewModelScope.launch { _sideEffects.send(HomeSideEffect.ShowToast(message)) }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { HomeViewModel() } }
    }
}
