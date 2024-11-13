package pl.marianjureczko.poszukiwacz.activity.searching

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.reset
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.port.XmlHelper

@Disabled("the SUT is to replace")
@ExtendWith(MockitoExtension::class)
class SearchingActivityViewModelTest {

    @Mock
    lateinit var storageHelper: StorageHelper

    @Mock
    lateinit var state: SavedStateHandle

    @Test
    fun `SHOULD load treasure bag from storage WHEN storage helper loads a one`() {
        //given
        val treasuresProgress = some<TreasuresProgress>()
        val fixture = SearchingActivityViewModelFixture(state)
        fixture.setupMockForGivenTreasureProgress(storageHelper, treasuresProgress)

        //when initialize model (in fixture)

        //then
//        assertThat(fixture.model.getRoute()).isEqualTo(fixture.route)
//        assertThat(fixture.model.getProgress()).usingRecursiveComparison().isEqualTo(treasuresProgress)
    }

    @Test
    fun `SHOULD save new treasure bag WHEN the treasure bag is replace`() {
        //given
//        val model = pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel(state)
//        val treasuresProgress = some<TreasuresProgress>()
//
//        //when
//        model.replaceProgress(treasuresProgress, storageHelper)
//
//        //then
//        then(storageHelper).should().save(treasuresProgress)
//        assertThat(model.getProgress()).usingRecursiveComparison().isEqualTo(treasuresProgress)
    }

    @Test
    fun `SHOULD save new treasure progress WHEN collecting next treasure`() {
        //given
        val fixture = SearchingActivityViewModelFixture(state)
        fixture.setupMockForEmptyTreasureBag(storageHelper)
        val treasure = some<Treasure>().copy(type = TreasureType.DIAMOND)
        reset(storageHelper)

        //when
//        fixture.model.collectTreasure(treasure, storageHelper)
//
//        //then
//        then(storageHelper).should().save(fixture.model.getProgress())
//        assertThat(fixture.model.getDiamonds()).isEqualTo(treasure.quantity.toString())
    }

    @Test
    fun `SHOULD say initialized WHEN the flag was set by calling getTreasureSelectorActivityInputData`() {
        //given
//        val model = some<pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel>()
//        model.getTreasureSelectorInputData(false, null)
//
//        //when
//        val actual = model.treasureSelectionInitialized()
//
//        //then
//        assertThat(actual).isTrue()
    }

    @Test
    fun `SHOULD say not initialized WHEN the flag is not set ie getTreasureSelectorActivityInputData was never called and selected is null`() {
        //given
        val fixture = SearchingActivityViewModelFixture(state)
        fixture.setupMockForEmptyTreasureBag(storageHelper)
//        assertThat(fixture.model.getSelectedForHuntTreasure()).isNull()
//
//        //when
//        val actual = fixture.model.treasureSelectionInitialized()
//
//        //then
//        assertThat(actual).isFalse()
    }

    @Test
    fun `SHOULD say initialized WHEN the flag is not set ie getTreasureSelectorActivityInputData was never called but selected is set`() {
        //given
        val fixture = SearchingActivityViewModelFixture(state)
        fixture.setupMockForGivenTreasureProgress(storageHelper, some<TreasuresProgress>())
//        assertThat(fixture.model.getSelectedForHuntTreasure()).isNotNull()
//
//        //when
//        val actual = fixture.model.treasureSelectionInitialized()
//
//        //then
//        assertThat(actual).isTrue()
    }

    @Test
    fun `SHOULD restore state WHEN instantiating view model`() {
        //given
//        pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel(state)
//
//        //then
//        then(state).should().get<Boolean>(pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel.TREASURE_SELECTION_INITIALIZED)
    }

    @Test
    fun `SHOULD persist state WHEN getting data from treasure selector launcher TO not reexecute the selection second time`() {
        //given
        val fixture = SearchingActivityViewModelFixture(state)
        fixture.setupMockForGivenTreasureProgress(storageHelper, some<TreasuresProgress>())
//        assertThat(fixture.model.getSelectedForHuntTreasure()).isNotNull()
//
//        //when
//        val actual = fixture.model.getTreasureSelectorInputData(false, null)
//
//        //then
//        then(state).should().set(pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel.TREASURE_SELECTION_INITIALIZED, true)
    }
}


data class SearchingActivityViewModelFixture(
    private val savedStateHandle: SavedStateHandle,
    private val xmlHelper: XmlHelper = XmlHelper(),
//    val model: pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel = pl.marianjureczko.poszukiwacz.activity.searching.SearchingActivityViewModel(
//        savedStateHandle
//    ),
    val route: Route = some<Route>(),
    val xml: String = xmlHelper.writeToString(route)
) {
    fun setupMockForEmptyTreasureBag(storageHelper: StorageHelper) {
        given(storageHelper.loadProgress(route.name))
            .willReturn(null)
//        model.initialize(xml, storageHelper)
    }

    fun setupMockForGivenTreasureProgress(storageHelper: StorageHelper, treasuresProgress: TreasuresProgress) {
        given(storageHelper.loadProgress(route.name))
            .willReturn(treasuresProgress)
//        model.initialize(xml, storageHelper)
    }

    private fun initializeModel(storageHelper: StorageHelper) {
//        model.initialize(xml, storageHelper)
    }
}
