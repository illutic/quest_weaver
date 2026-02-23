package gr.questweaver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.ai.AiRoute
import gr.questweaver.navigation.NavigationController
import gr.questweaver.navigation.NavControllerType
import gr.questweaver.navigation.Route
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named

class HomeDashboardViewModel : ViewModel(), KoinComponent {
    private val homeNavController: NavigationController by inject()
    private val appNavController: NavigationController by inject(named(NavControllerType.App))
    private val sheetNavController: NavigationController by inject(named(NavControllerType.Sheet))

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()
    val homeNavigationState = homeNavController.state

    private val _sideEffects = Channel<HomeSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    init {
        viewModelScope.launch {
            val strings = loadHomeStrings()
            homeNavController.navigateTo(HomeRoute.Home)

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
            is HomeEvent.OnGameClick -> emitToast("Game ${event.gameId} Clicked!")
            is HomeEvent.OnCreateGameClick -> navigateToSheet(HomeRoute.CreateGame)
            is HomeEvent.OnRecentGamesViewAllClick -> navigateTo(HomeRoute.RecentGames)
            is HomeEvent.OnAiAssistantClick -> navigateTo(AiRoute.AiAssistant)
            is HomeEvent.OnResourceClick -> {
                val resource = state.value.resources.find { it.id == event.resourceId }
                if (resource != null) {
                    _state.update { it.copy(selectedResource = resource) }
                    val title = resource.title
                    navigateToSheet(HomeRoute.ResourceDetails(event.resourceId, title))
                }
            }

            is HomeEvent.OnResourcesViewAllClick -> navigateTo(HomeRoute.ResourcesList)
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
            it.copy(recentGames = updatedGames)
        }
        dismissSheet()
    }

    private fun navigateTo(route: Route) {
        when (route) {
            is HomeRoute -> homeNavController.navigateTo(route)
            else -> appNavController.navigateTo(route)
        }
    }

    private fun navigateToSheet(route: Route) {
        sheetNavController.navigateTo(route)
    }

    private fun navigateBack() {
        when (homeNavController.state.value.currentRoute) {
            is HomeRoute -> homeNavController.navigateBack()
            else -> appNavController.navigateBack()
        }
    }

    private fun dismissSheet() {
        sheetNavController.clearBackStack()
    }

    private fun emitToast(message: String) {
        viewModelScope.launch { _sideEffects.send(HomeSideEffect.ShowToast(message)) }
    }

    companion object Companion {
        fun createFactory() = viewModelFactory { initializer { HomeDashboardViewModel() } }
    }
}
