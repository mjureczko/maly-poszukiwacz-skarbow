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
import pl.marianjureczko.poszukiwacz.compass.api.CompassIoDispatcher
import pl.marianjureczko.poszukiwacz.compass.api.CompassMainDispatcher
import pl.marianjureczko.poszukiwacz.compass.api.LocationPort
import pl.marianjureczko.poszukiwacz.screen.facebook.ReportStoragePort
import pl.marianjureczko.poszukiwacz.screen.searching.QrScannerPort
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.external.ExternalStoragePort
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort
import javax.inject.Singleton

/**
 * Module providing beans that shall be overridden in espresso tests.
 */
@Module
@InstallIn(SingletonComponent::class)
object PortsModule {

    // Dagger does not allow more than one @Qualifier per @Provides method,
    // so the app and compass qualifiers get separate providers.
    @Provides
    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @CompassIoDispatcher
    fun compassIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @MainDispatcher
    fun mainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Provides
    @CompassMainDispatcher
    fun compassMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

    @Singleton
    @Provides
    fun storagePort(@ApplicationContext appContext: Context): StoragePort {
        return StoragePort(appContext)
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
        @CompassIoDispatcher ioDispatcher: CoroutineDispatcher,
        @CompassMainDispatcher mainDispatcher: CoroutineDispatcher
    ): LocationPort {
        return LocationPort.create(appContext, locationClient, ioDispatcher, mainDispatcher)
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

    @Singleton
    @Provides
    fun achievementsStoragePort(@ApplicationContext appContext: Context): AchievementsStoragePort {
        return ExternalStoragePort(appContext)
    }

    @Singleton
    @Provides
    fun reportStoragePort(@ApplicationContext appContext: Context): ReportStoragePort {
        return ExternalStoragePort(appContext)
    }
}