package pl.marianjureczko.poszukiwacz.shared.di

import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PortsModule {

    @Singleton
    @Provides
    fun fusedLocationClient(@ApplicationContext context: Context): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

    @Singleton
    @Provides
    fun locationPort(
        @ApplicationContext appContext: Context,
        locationClient: FusedLocationProviderClient,
        @IoDispatcher ioDispatcher: CoroutineDispatcher,
        @MainDispatcher mainDispatcher: CoroutineDispatcher
    ): LocationPort {
        return LocationPort(appContext, locationClient, ioDispatcher, mainDispatcher)
    }

    @Singleton
    @Provides
    fun photoPort(
        @ApplicationContext appContext: Context
    ): CameraPort {
        return CameraPort(appContext)
    }
}