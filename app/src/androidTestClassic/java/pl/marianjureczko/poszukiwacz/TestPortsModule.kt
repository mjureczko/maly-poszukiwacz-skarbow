package pl.marianjureczko.poszukiwacz

import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.RouteArranger
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestPortsModule {

    val storage = TestStoragePort()
    val locationClient = mock<FusedLocationProviderClient>()
    val location = TestLocationPort()
    val camera = TestCameraPort()

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
    fun storageHelper(): StorageHelper {
        return storage
    }



    fun getRouteFromStorage() = storage.routes.values.first()

    fun assureRouteIsPresentInStorage() {
        if (storage.routes.isEmpty()) {
            val newRoute = RouteArranger.routeWithoutTipFiles()
            storage.routes[newRoute.name] = newRoute
        }
    }
}