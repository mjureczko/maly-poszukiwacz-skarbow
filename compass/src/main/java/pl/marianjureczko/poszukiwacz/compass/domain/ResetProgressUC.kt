package pl.marianjureczko.poszukiwacz.compass.domain

import androidx.compose.runtime.MutableState
import pl.marianjureczko.poszukiwacz.compass.state.CompassState

class ResetProgressUC {
    operator fun invoke(state: MutableState<CompassState>) {
        state.value = CompassState()
    }
}