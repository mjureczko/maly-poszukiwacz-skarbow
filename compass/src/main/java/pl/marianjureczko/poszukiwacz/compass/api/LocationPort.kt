package pl.marianjureczko.poszukiwacz.compass.api

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import pl.marianjureczko.poszukiwacz.compass.data.LocationPortImpl

interface LocationPort {
    companion object {
        fun create(
            context: Context,
            locationClient: FusedLocationProviderClient,
            ioDispatcher: CoroutineDispatcher,
            mainDispatcher: CoroutineDispatcher
        ): LocationPort = LocationPortImpl(context, locationClient, ioDispatcher, mainDispatcher)
    }

    fun startFetching(
        coroutineScope: CoroutineScope,
        updateLocationCallback: (AndroidLocation) -> Unit
    )

    fun stopFetching()
}