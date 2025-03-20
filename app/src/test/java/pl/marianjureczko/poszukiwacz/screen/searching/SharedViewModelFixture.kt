package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import kotlinx.coroutines.CoroutineDispatcher
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescriptionArranger
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper

data class SharedViewModelFixture(
    val testDispatcher: CoroutineDispatcher,
    val routeName: String = someString(),
    val firstTreasureQrCode: String = TreasureDescriptionArranger.validQrCode(),
    val storage: StorageHelper = mock(StorageHelper::class.java),
    val locationPort: LocationPort = mock(LocationPort::class.java),
    val locationCalculator: LocationCalculator = mock(),
    val savedState: SavedStateHandle = mock(SavedStateHandle::class.java),
    val photoHelper: PhotoHelper = mock(PhotoHelper::class.java),
    val cameraPort: CameraPort = mock(CameraPort::class.java),
    val qrScannerPort: QrScannerPort = mock(QrScannerPort::class.java),
) {

    lateinit var route: Route

    fun givenMocksForNoProgress(): SharedViewModel {
        BDDMockito.given(savedState.get<String>(PARAMETER_ROUTE_NAME)).willReturn(routeName)
        route = some<Route>().copy(name = routeName)
        route.treasures.first().qrCode = firstTreasureQrCode
        BDDMockito.given(storage.loadRoute(routeName)).willReturn(route)
        BDDMockito.given(storage.loadProgress(routeName)).willReturn(null)
        val result = SharedViewModel(
            storageHelper = storage,
            locationPort = locationPort,
            locationCalculator = locationCalculator,
            photoHelper = photoHelper,
            stateHandle = savedState,
            cameraPort = cameraPort,
            qrScannerPort = qrScannerPort,
            ioDispatcher = testDispatcher,
        )
        result.respawn = false;
        return result
    }

    fun givenScanResultForFirstTreasure(): String {
        return firstTreasureQrCode
    }
}