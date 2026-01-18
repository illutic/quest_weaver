package gr.questweaver.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.ai.AiRoute
import gr.questweaver.bottombar.BottomBarController
import gr.questweaver.bottombar.BottomBarIcon
import gr.questweaver.bottombar.BottomBarItem
import gr.questweaver.bottombar.BottomBarMode
import gr.questweaver.navigation.NavigationController
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

class HomeViewModel : ViewModel(), KoinComponent {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private val _sideEffects = Channel<HomeSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    private val bottomBarController: BottomBarController by inject()
    val navigationController: NavigationController by inject()

    init {
        viewModelScope.launch {
            val strings = loadHomeStrings()

            // Set Bottom Bar Items
            bottomBarController.setItems(
                    listOf(
                            BottomBarItem(
                                    strings.navDashboard,
                                    BottomBarIcon.Home,
                                    HomeRoute.Home,
                                    true
                            ),
                            BottomBarItem(
                                    strings.navSearch,
                                    BottomBarIcon.Search,
                                    HomeRoute.Search,
                                    false
                            ),
                            BottomBarItem(
                                    strings.navSettings,
                                    BottomBarIcon.Settings,
                                    HomeRoute.Settings,
                                    false
                            )
                    )
            )
            bottomBarController.setMode(BottomBarMode.Standard)

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
            is HomeEvent.OnAiAssistantClick -> navigateTo(AiRoute.AiAssistant)
            is HomeEvent.OnResourceClick -> {
                val resource = state.value.resources.find { it.id == event.resourceId }
                if (resource != null) {
                    _state.update { it.copy(selectedResource = resource) }
                    val title = resource.title
                    navigateTo(HomeRoute.ResourceDetails(event.resourceId, title))
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
        navigationController.navigateBack()
    }

    private fun navigateTo(route: Route) {
        navigationController.navigateTo(route)
    }

    private fun navigateBack() {
        navigationController.navigateBack()
    }

    private fun dismissSheet() {
        navigationController.navigateBack()
    }

    private fun emitToast(message: String) {
        viewModelScope.launch { _sideEffects.send(HomeSideEffect.ShowToast(message)) }
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { HomeViewModel() } }
    }
}
