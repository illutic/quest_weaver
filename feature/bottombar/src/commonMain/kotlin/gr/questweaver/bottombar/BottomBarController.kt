package gr.questweaver.bottombar

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BottomBarController {

    private val _state = MutableStateFlow(BottomBarState())
    val state: StateFlow<BottomBarState> = _state.asStateFlow()

    private val _events =
        MutableSharedFlow<BottomBarEvent>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val events: SharedFlow<BottomBarEvent> = _events.asSharedFlow()

    fun setItems(items: List<BottomBarItem>) {
        _state.update { it.copy(items = items) }
    }

    fun setMode(mode: BottomBarMode) {
        _state.update { it.copy(mode = mode) }
    }

    fun setInputValue(value: String) {
        _state.update { it.copy(inputValue = value) }
    }

    /* internal */ fun emitEvent(event: BottomBarEvent) {
        _events.tryEmit(event)
    }
}
