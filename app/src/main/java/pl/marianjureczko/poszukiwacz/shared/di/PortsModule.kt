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
import kotlinx.coroutines.Dispatchers
import pl.marianjureczko.poszukiwacz.screen.searching.QrScannerPort
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PortsModule {

    @Provides
    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Singleton
    @Provides
    fun storageHelper(@ApplicationContext appContext: Context): StorageHelper {
        return StorageHelper(appContext)
    }

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

    @Singleton
    @Provides
    fun qrScannerPort(): QrScannerPort {
        return QrScannerPort()
    }
}