package gr.questweaver.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gr.questweaver.user.domain.usecase.IsUserRegisteredUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class OnboardingViewModel : ViewModel(), KoinComponent {
    private val isUserRegisteredUseCase: IsUserRegisteredUseCase by inject()

    private val _state = MutableStateFlow(OnboardingState())
    val state = _state.asStateFlow()

    private fun loadOnboardingState() = viewModelScope.launch {
        val isRegistered = isUserRegisteredUseCase().getOrElse { false }
        _state.update {
            it.copy(
                isRegistered = isRegistered
            )
        }
    }

    init {
        loadOnboardingState()
    }
}