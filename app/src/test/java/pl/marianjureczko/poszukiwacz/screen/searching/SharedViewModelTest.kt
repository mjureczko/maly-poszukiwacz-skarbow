package pl.marianjureczko.poszukiwacz.screen.searching

import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.somePositiveInt
import com.ocadotechnology.gembus.test.someString
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.times
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.atLeastOnce
import pl.marianjureczko.poszukiwacz.any
import pl.marianjureczko.poszukiwacz.eq
import pl.marianjureczko.poszukiwacz.model.HunterLocation
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.TreasureDescriptionArranger
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.result.ResultType
import pl.marianjureczko.poszukiwacz.shared.Coordinates
import pl.marianjureczko.poszukiwacz.shared.port.LocationPort


@ExtendWith(MockitoExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class SharedViewModelTest {

    private val dispatcher: TestDispatcher = StandardTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Test
    fun `SHOULD remove commemorative photos WHEN restarting progress`() {
        //given
        val fixture = SharedViewModelFixture(dispatcher/*, storage = storage*/)
        val progress = some<TreasuresProgress>().apply {
            routeName = fixture.routeName
        }
        val viewModel = fixture.givenMocksForProgress(progress)

        //when
        viewModel.restartProgress()

        //then
        progress.commemorativePhotosByTreasuresDescriptionIds.values.forEach {
            then(fixture.storage).should(times(1)).removeFile(it)
        }
        assertThat(viewModel.state.value.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds).isEmpty()
        assertThat(viewModel.state.value.treasuresProgress.selectedTreasureDescriptionId).isEqualTo(progress.selectedTreasureDescriptionId)
        assertThat(viewModel.state.value.treasuresProgress.routeName).isEqualTo(fixture.routeName)
    }

    @Test
    fun `SHOULD remove data from progress and persist it WHEN restarting progress`() {
        // given
        val fixture = SharedViewModelFixture(dispatcher)
        val progress = some<TreasuresProgress>().apply {
            routeName = fixture.routeName
        }
        val viewModel = fixture.givenMocksForProgress(progress)

        // when
        viewModel.restartProgress()

        // then
        val updatedProgress = viewModel.state.value.treasuresProgress
        assertThat(updatedProgress.justFoundTreasureId).isNull()
        assertThat(updatedProgress.resultRequiresPresentation).isFalse()
        assertThat(updatedProgress.treasureFoundGoToSelector).isFalse()
        assertThat(updatedProgress.collectedQrCodes).isEmpty()
        assertThat(updatedProgress.collectedTreasuresDescriptionId).isEmpty()
        assertThat(updatedProgress.knowledge).isEqualTo(0)
        assertThat(updatedProgress.golds).isEqualTo(0)
        assertThat(updatedProgress.rubies).isEqualTo(0)
        assertThat(updatedProgress.diamonds).isEqualTo(0)
        assertThat(updatedProgress.routeName).isEqualTo(progress.routeName)
        assertThat(updatedProgress.selectedTreasureDescriptionId).isEqualTo(progress.selectedTreasureDescriptionId)
        then(fixture.storage).should(times(1)).save(updatedProgress)
    }

    @Test
    fun `SHOULD remove data from hunterPath and persist it WHEN restarting progress`() {
        // given
        val fixture = SharedViewModelFixture(dispatcher)
        val progress = some<TreasuresProgress>().apply {
            routeName = fixture.routeName
        }
        val viewModel = fixture.givenMocksForProgress(progress)

        // when
        viewModel.restartProgress()

        //then
        val emptyHunterPath = HunterPath(fixture.routeName)
        assertThat(viewModel.state.value.hunterPath)
            .usingRecursiveComparison()
            .isEqualTo(emptyHunterPath)
        val captor = argumentCaptor<HunterPath>()
        then(fixture.storage).should(atLeastOnce()).save(captor.capture())
        assertThat(captor.lastValue)
            .usingRecursiveComparison()
            .isEqualTo(emptyHunterPath)
    }

    @Test
    fun `SHOULD update location`() = scope.runTest {
        //given
        val context = mock(Context::class.java)
        val locationProvider = mock(FusedLocationProviderClient::class.java)
        val locationPort = LocationPort(context, locationProvider, dispatcher, dispatcher)
        val fixture = SharedViewModelFixture(dispatcher, locationPort = locationPort)
        val captor = argumentCaptor<LocationCallback>()
        given(locationProvider.requestLocationUpdates(any(LocationRequest::class.java), captor.capture(), eq(null)))
            .willReturn(mock())
        val viewModel = fixture.givenMocksForNoProgress()

        val location: Location = mock()
        val locationResult: LocationResult = mock()
        val latitude = viewModel.state.value.selectedTreasureDescription()!!.latitude
        val longitude = viewModel.state.value.selectedTreasureDescription()!!.longitude - 1
        given(location.getLatitude()).willReturn(latitude)
        given(location.getLongitude()).willReturn(longitude)
        val expectedDistance = somePositiveInt(999_999)
        given(
            fixture.locationCalculator.distanceInSteps(
                viewModel.state.value.selectedTreasureDescription()!!, location
            )
        ).willReturn(expectedDistance)

        //when
        advanceTimeBy(100L)
        val locationCallback = captor.firstValue
        given(locationResult.getLastLocation()).willReturn(location)
        locationCallback.onLocationResult(locationResult)

        //then
        assertThat(viewModel.state.value.currentLocation).isEqualTo(location)
        assertThat(viewModel.state.value.stepsToTreasure).isEqualTo(expectedDistance)
        assertThat(viewModel.state.value.needleRotation).isCloseTo(90.0f, Offset.offset(0.01f))
        assertThat(viewModel.state.value.hunterPath.publicLocations)
            .containsExactly(HunterLocation(Coordinates(latitude, longitude)))
    }

    /**
     * private val scope = TestScope(dispatcher); ... scope.runTest {...} is not used to disable corutines execution
     */
    @Test
    fun `SHOULD not call goToResults WHEN there is no result of qr code scanning`() {
        //given
        val fixture = SharedViewModelFixture(dispatcher)
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = ""

        //when
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { _: String, _: ResultType, _: Int?, _: Int? ->
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
        val fixture = SharedViewModelFixture(dispatcher)
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = someString()

        //when & then
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { _, actual, _, _ ->
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
        val qrCode = TreasureDescriptionArranger.validQrCode("k")
        val fixture = SharedViewModelFixture(dispatcher, firstTreasureQrCode = qrCode)
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = fixture.givenScanResultForFirstTreasure()

        //when
        var wasCalled = false
        val callback = viewModel.scannedTreasureCallback { _, actual, _, _ ->
            wasCalled = true
            assertThat(actual).isEqualTo(ResultType.KNOWLEDGE)
        }
        callback(qrResult)

        //then
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
        val fixture = SharedViewModelFixture(dispatcher)
        val viewModel = fixture.givenMocksForNoProgress()
        val qrResult = fixture.givenScanResultForFirstTreasure()

        //when & then
        var callbackUsed = 0
        val callback1 = viewModel.scannedTreasureCallback { _, actual, _, _ ->
            callbackUsed++
        }
        callback1(qrResult)
        val callback2 = viewModel.scannedTreasureCallback { _, actual, _, _ ->
            callbackUsed++
            assertThat(actual).isEqualTo(ResultType.ALREADY_TAKEN)
        }
        callback2(qrResult)


        assertThat(callbackUsed).isEqualTo(2)
        // there should be no 3rd save for the already taken treasure
        then(fixture.storage).should(times(2)).save(any(TreasuresProgress::class.java))
    }
}