package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CustomMainViewModel @Inject constructor(
    resources: Resources
) : ViewModel() {
    private var _state = mutableStateOf(CustomMainState(resources))

    val state: State<CustomMainState>
        get() = _state

    fun nextLeadMessage() {
        if (_state.value.messageIndex + 1 < _state.value.messages.size) {
            _state.value = _state.value.copy(messageIndex = _state.value.messageIndex + 1)
        }
    }
}