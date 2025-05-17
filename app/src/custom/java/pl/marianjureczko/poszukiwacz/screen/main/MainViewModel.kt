package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.Resources
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    resources: Resources,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val customInitializerForRoute: CustomInitializerForRoute
) : ViewModel() {
    private var _state = mutableStateOf(MainState(resources))

    val state: State<MainState>
        get() = _state

    fun initializeAssets() {
        viewModelScope.launch(dispatcher) {
            customInitializerForRoute.copyRouteToLocalStorage()
            _state.value = _state.value.copy(assetsCopied = true)
        }
    }

    fun nextLeadMessage() {
        if (_state.value.messageIndex + 1 < _state.value.messages.size) {
            _state.value = _state.value.copy(messageIndex = _state.value.messageIndex + 1)
        }
    }

    fun prevLeadMessage() {
        if (_state.value.messageIndex > 0) {
            _state.value = _state.value.copy(messageIndex = _state.value.messageIndex - 1)
        }
    }

    fun restartMessages() {
        viewModelScope.launch(dispatcher) {
            delay(1_000)
            _state.value = _state.value.copy(messageIndex = 0)
        }
    }
}