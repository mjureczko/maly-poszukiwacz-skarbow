package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.journeyapps.barcodescanner.ScanIntentResult
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import kotlinx.coroutines.CoroutineDispatcher
import org.mockito.BDDMockito
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

data class SearchingViewModelFixture(
    val dispatcher: CoroutineDispatcher,
    val routeName: String = someString(),
    val firstTreasureQrCode: String = someString(),
    val storage: StorageHelper = mock(StorageHelper::class.java),
    val locationFetcher: LocationFetcher = mock(LocationFetcher::class.java),
    val savedState: SavedStateHandle = mock(SavedStateHandle::class.java),
    val photoHelper: PhotoHelper = mock(PhotoHelper::class.java)
) {

    lateinit var route: Route

    fun givenMocksForNoProgress(): SharedViewModel {
        BDDMockito.given(savedState.get<String>(PARAMETER_ROUTE_NAME)).willReturn(routeName)
        route = some<Route>().copy(name = routeName)
        route.treasures.first().qrCode = firstTreasureQrCode
        BDDMockito.given(storage.loadRoute(routeName)).willReturn(route)
        BDDMockito.given(storage.loadProgress(routeName)).willReturn(null)
        BDDMockito.given(photoHelper.getCommemorativePhotoTempUri()).willReturn(mock(Uri::class.java))
        return SharedViewModel(storage, locationFetcher,
            pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator(), photoHelper, savedState, dispatcher)
    }

    fun givenScanIntentResultForFirstTreasure(): ScanIntentResult {
        return some<ScanIntentResult>(overrides = mapOf("contents" to { firstTreasureQrCode }))
    }
}