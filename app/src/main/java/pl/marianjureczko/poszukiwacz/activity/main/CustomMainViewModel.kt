package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomMainViewModel @Inject constructor(
    resources: Resources,
    val dispatcher: CoroutineDispatcher,
    customInitializerForRoute: CustomInitializerForRoute
) : ViewModel() {
    private var _state = mutableStateOf(CustomMainState(resources))

    init {
        viewModelScope.launch(dispatcher) {
            customInitializerForRoute.copyRouteToLocalStorage()
        }
    }

    val state: State<CustomMainState>
        get() = _state

    fun nextLeadMessage() {
        if (_state.value.messageIndex + 1 < _state.value.messages.size) {
            _state.value = _state.value.copy(messageIndex = _state.value.messageIndex + 1)
        }
    }

    fun restartMessages() {
        viewModelScope.launch(dispatcher) {
            delay(1_000)
            _state.value = _state.value.copy(messageIndex = 0)
        }
    }
}