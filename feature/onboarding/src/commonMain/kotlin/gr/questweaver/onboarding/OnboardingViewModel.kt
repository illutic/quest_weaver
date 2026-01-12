package gr.questweaver.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.questweaver.navigation.Route
import gr.questweaver.user.domain.usecase.GenerateUsernameUseCase
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import gr.questweaver.user.domain.usecase.SetUserUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    fun registerUser(name: String) =
        viewModelScope.launch {
            setUserUseCase(name).onSuccess {
                _state.update { state -> state.copy(isRegistered = true) }
            }
        }

    private fun loadOnboardingState() =
        viewModelScope.launch {
            val isRegistered = isUserRegisteredUseCase().getOrElse { false }
            _state.update { it.copy(isRegistered = isRegistered) }
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

    fun onNameChange(name: String) {
        _state.update { it.copy(name = name) }
    }

    fun generateRandomName() {
        _state.update { it.copy(name = generateUsernameUseCase()) }
    }

    init {
        loadOnboardingState()
    }
}
