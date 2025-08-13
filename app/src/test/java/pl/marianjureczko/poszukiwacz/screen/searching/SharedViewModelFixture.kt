package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import kotlinx.coroutines.CoroutineDispatcher
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescriptionArranger
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort
import pl.marianjureczko.poszukiwacz.shared.port.location.AndroidLocationFactoryImpl
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort
import pl.marianjureczko.poszukiwacz.usecase.ResetProgressUC
import pl.marianjureczko.poszukiwacz.usecase.UpdateLocationUC

data class SharedViewModelFixture(
    val testDispatcher: CoroutineDispatcher,
    val routeName: String = someString(),
    val firstTreasureQrCode: String = TreasureDescriptionArranger.validQrCode(),
    val storage: StoragePort = mock(StoragePort::class.java),
    val locationPort: LocationPort = mock(LocationPort::class.java),
    val locationCalculator: LocationCalculator = mock(),
    val savedState: SavedStateHandle = mock(SavedStateHandle::class.java),
    val photoHelper: PhotoHelper = mock(PhotoHelper::class.java),
    val cameraPort: CameraPort = mock(CameraPort::class.java),
    val qrScannerPort: QrScannerPort = mock(QrScannerPort::class.java),
) {

    val resetProgressUC = ResetProgressUC(storage)

    var route: Route = some<Route>().copy(name = routeName)

    fun givenMocksForNoProgress(): SharedViewModel {
        BDDMockito.given(storage.loadProgress(routeName)).willReturn(null)
        return getSharedViewModel()
    }

    fun givenMocksForProgress(progress: TreasuresProgress): SharedViewModel {
        BDDMockito.given(storage.loadProgress(routeName)).willReturn(progress)
        return getSharedViewModel()
    }

    private fun getSharedViewModel(): SharedViewModel {
        BDDMockito.given(savedState.get<String>(Screens.Searching.PARAMETER_ROUTE_NAME)).willReturn(routeName)
        route.treasures.first().qrCode = firstTreasureQrCode
        BDDMockito.given(storage.loadRoute(routeName)).willReturn(route)
        val result = SharedViewModel(
            storage = storage,
            locationPort = locationPort,
            photoHelper = photoHelper,
            stateHandle = savedState,
            cameraPort = cameraPort,
            locationCalculator = LocationCalculator(AndroidLocationFactoryImpl()),
            qrScannerPort = qrScannerPort,
            resetProgressUC = resetProgressUC,
            updateLocationUC = UpdateLocationUC(storage, locationCalculator),
            ioDispatcher = testDispatcher,
        )
        result.respawn = false
        return result
    }

    fun givenScanResultForFirstTreasure(): String {
        return firstTreasureQrCode
    }
}