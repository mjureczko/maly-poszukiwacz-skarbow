package pl.marianjureczko.poszukiwacz.activity.searching.n

import android.content.res.Resources
import androidx.lifecycle.SavedStateHandle
import com.journeyapps.barcodescanner.ScanIntentResult
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.any
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

@ExtendWith(MockitoExtension::class)
class SharedViewModelTest {

    @Mock
    lateinit var storage: StorageHelper

    @Mock
    lateinit var locationFetcher: LocationFetcher

    @Mock
    lateinit var savedState: SavedStateHandle

    @Mock
    lateinit var resources: Resources

    @Test
    fun `SHOULD not call goToResults WHEN there is no  result of qr code scanning`() {
        //given
        val fixture = SearchingViewModelFixture()
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = some<ScanIntentResult>(overrides = mapOf("contents" to { "" }))

        //when
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { actual ->
            wasCalled = true
        }
        callback(qrResult)

        //then
        assertThat(wasCalled).isFalse()
        // should save empty progress when loading new route
        then(fixture.storage).should(times(1)).save(
            viewModel.state.value.treasuresProgress
        )
    }

    @Test
    fun `SHOULD return not a treasure WHEN the scanned qr code is not associated with any treasure`() {
        //given
        val fixture = SearchingViewModelFixture()
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = some<ScanIntentResult>(overrides = mapOf("contents" to { someString() }))

        //when & then
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { actual ->
            wasCalled = true
            assertThat(actual).isEqualTo(ResultType.NOT_A_TREASURE)
        }
        callback(qrResult)

        assertThat(wasCalled).isTrue()
        // should save empty progress when loading new route
        then(fixture.storage).should(times(1)).save(
            viewModel.state.value.treasuresProgress
        )
    }

    @Test
    fun `SHOULD return a treasure WHEN the scanned qr code is not associated with a treasure`() {
        //given
        val qrCode = someString()
        val fixture = SearchingViewModelFixture(firstTreasureQrCode = qrCode)
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = fixture.givenScanIntentResultForFirstTreasure()

        //when & then
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { actual ->
            wasCalled = true
            assertThat(actual).isEqualTo(ResultType.TREASURE)
        }
        callback(qrResult)

        assertThat(wasCalled).isTrue()
        // should save empty progress when loading new route and later updated with first treasure collected
        then(fixture.storage).should(times(2)).save(any(TreasuresProgress::class.java))
        assertThat(viewModel.state.value.treasuresProgress.collectedTreasuresDescriptionId)
            .containsExactly(viewModel.state.value.route.treasures.first().id)
        assertThat(viewModel.state.value.treasuresProgress.collectedQrCodes).containsExactly(qrCode)
    }

    @Test
    fun `SHOULD return already taken WHEN the scanned qr code is scanned twice`() {
        //given
        val fixture = SearchingViewModelFixture()
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = fixture.givenScanIntentResultForFirstTreasure()

        //when & then
        var callbackUsed = 0
        val callback1 = viewModel.scannedTreasureCallback { actual ->
            callbackUsed++
        }
        callback1(qrResult)
        val callback2 = viewModel.scannedTreasureCallback { actual ->
            callbackUsed++
            assertThat(actual).isEqualTo(ResultType.ALREADY_TAKEN)
        }
        callback2(qrResult)


        assertThat(callbackUsed).isEqualTo(2)
        // there should be no 3rd save for the already taken treasure
        then(fixture.storage).should(times(2)).save(any(TreasuresProgress::class.java))
    }
}