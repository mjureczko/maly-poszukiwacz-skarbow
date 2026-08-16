package pl.marianjureczko.poszukiwacz.compass.data

import kotlinx.coroutines.CoroutineScope

interface LocationPort {
    fun startFetching(
        viewModelScope: CoroutineScope,
        updateLocationCallback: UpdateLocationCallback
    )

    fun stopFetching()
}