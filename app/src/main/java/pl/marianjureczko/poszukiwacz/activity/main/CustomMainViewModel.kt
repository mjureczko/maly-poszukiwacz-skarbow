package pl.marianjureczko.poszukiwacz.activity.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class CustomMainViewModel : ViewModel() {
    private var _state = mutableStateOf(CustomMainState())

    val state: State<CustomMainState>
        get() = _state

    fun nextLeadMessage() {
        if(_state.value.messageIndex + 1 < _state.value.messages.size) {
            _state.value = _state.value.copy(messageIndex = _state.value.messageIndex + 1)
        }
    }
}