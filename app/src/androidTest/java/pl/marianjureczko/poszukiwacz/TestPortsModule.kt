package pl.marianjureczko.poszukiwacz

import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.screen.searching.QrScannerPort
import pl.marianjureczko.poszukiwacz.shared.di.IoDispatcher
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort
import pl.marianjureczko.poszukiwacz.usecase.badges.TestAchievementsStoragePort
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestPortsModule {

    val locationClient = mock<FusedLocationProviderClient>()
    val location = TestLocationPort()
    val camera = TestCameraPort()
    val qrScannerPort = TestQrScannerPort()
    val achievementsStoragePort = TestAchievementsStoragePort()
    var ioDispatcher = StandardTestDispatcher()

    @Provides
    @IoDispatcher
    fun ioDispatcher(): CoroutineDispatcher = ioDispatcher //Dispatchers.IO

    @Singleton
    @Provides
    fun fusedLocationClient(): FusedLocationProviderClient {
        return locationClient
    }

    @Singleton
    @Provides
    fun locationPort(): LocationPort {
        return location
    }

    @Singleton
    @Provides
    fun photoPort(): CameraPort {
        return camera
    }

    @Singleton
    @Provides
    fun qrScannerPort(): QrScannerPort {
        return qrScannerPort
    }

    @Singleton
    @Provides
    fun achievementsStoragePort(): AchievementsStoragePort {
        return achievementsStoragePort
    }
}
