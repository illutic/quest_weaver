package gr.questweaver.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gr.questweaver.home.HomeRoute
import gr.questweaver.navigation.Route
import gr.questweaver.user.domain.usecase.GenerateUsernameUseCase
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import gr.questweaver.user.domain.usecase.SetUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnboardingViewModel : ViewModel(), KoinComponent {
    private val isUserRegisteredUseCase: IsUserRegisteredUseCase by inject()
    private val setUserUseCase: SetUserUseCase by inject()
    private val generateUsernameUseCase: GenerateUsernameUseCase by inject()

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private val _sideEffects = Channel<OnboardingSideEffect>()
    val sideEffects = _sideEffects.receiveAsFlow()

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.OnNameChange -> onNameChange(event.name)
            OnboardingEvent.OnGenerateRandomName -> generateRandomName()
            is OnboardingEvent.OnRegisterClick -> registerUser(event.name)
            OnboardingEvent.OnCompleteOnboarding -> completeOnboarding()
            is OnboardingEvent.OnNavigate -> navigateTo(event.route)
            OnboardingEvent.OnNavigateBack -> navigateBack()
            OnboardingEvent.OnClearError -> clearError()
        }
    }

    private fun registerUser(name: String) =
        viewModelScope.launch {
            setUserUseCase(name)
                .onSuccess {
                    // Registration successful - user will navigate to tutorial via
                    // OnNavigate event
                }
                .onFailure { error ->
                    _state.update { state ->
                        state.copy(error = error.message ?: state.strings.errorUnknown)
                    }
                }
        }

    private fun loadOnboardingState() =
        viewModelScope.launch {
            val strings = loadOnboardingStrings()
            val isRegistered = isUserRegisteredUseCase().getOrElse { false }
            _state.update { it.copy(strings = strings) }

            // If already registered, navigate to home
            if (isRegistered) {
                _sideEffects.send(OnboardingSideEffect.Navigate(HomeRoute.Graph))
            }
        }

    private fun navigateTo(route: Route) {
        _state.update { it.copy(backStack = it.backStack + route) }
    }

    private fun navigateBack() {
        _state.update {
            if (it.backStack.size > 1) {
                it.copy(backStack = it.backStack.dropLast(1))
            } else {
                it
            }
        }
    }

    private fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    private fun generateRandomName() {
        _state.update { it.copy(name = generateUsernameUseCase()) }
    }

    private fun clearError() {
        _state.update { it.copy(error = null) }
    }

    private fun completeOnboarding() {
        viewModelScope.launch { _sideEffects.send(OnboardingSideEffect.Navigate(HomeRoute.Graph)) }
    }

    init {
        loadOnboardingState()
    }

    companion object {
        fun createFactory() = viewModelFactory { initializer { OnboardingViewModel() } }
    }
}
