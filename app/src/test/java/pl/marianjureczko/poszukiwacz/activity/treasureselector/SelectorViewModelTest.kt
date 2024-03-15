package pl.marianjureczko.poszukiwacz.activity.treasureselector

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import java.io.File
import java.nio.file.Files

@ExtendWith(MockitoExtension::class)
class SelectorViewModelStateTest {

    @Mock(lenient = true)
    lateinit var state: SavedStateHandle

    @Test
    fun `SHOULD override collected treasures ids WHEN corresponding data is available in state`() {
        //given
        val selectorViewModel = SelectorViewModel(state)
        val collectedIds = setOf(someInt())
        given(state.get<Set<Int>>(SelectorViewModel.IDS_OF_COLLECTED))
            .willReturn(collectedIds)

        //when
        selectorViewModel.initialize(some<Route>(), some<TreasuresProgress>(), false, null, null)

        //then
        assertThat(selectorViewModel.getIdsOfCollectedTreasures()).containsExactlyInAnyOrderElementsOf(collectedIds)
    }

    @Test
    fun `SHOULD not alter collected treasures ids WHEN corresponding data is not available in state`() {
        //given
        val selectorViewModel = SelectorViewModel(state)
        given(state.get<Set<Int>>(SelectorViewModel.IDS_OF_COLLECTED))
            .willReturn(null)
        val progress = some<TreasuresProgress>()
        progress.collectedTreasuresDescriptionId.add(someInt())
        val collectedIds = progress.collectedTreasuresDescriptionId.toSet()

        //when
        selectorViewModel.initialize(some<Route>(), progress, false, null, null)

        //then
        assertThat(selectorViewModel.getIdsOfCollectedTreasures()).containsExactlyInAnyOrderElementsOf(collectedIds)
    }
}

@Disabled("the SUT is to replace")
internal class SelectorViewModelTest {

    @Test
    fun `SHOULD select treasure in progress by id WHEN treasure with requested id exists in route`() {
        //given
        val model = some<SelectorViewModel>()
        val treasureToSelect = model.getTreasureDescriptionByPosition(0)

        //when
        model.selectTreasureById(treasureToSelect.id)

        //then
        assertThat(model.getSelectedTreasure()).isEqualTo(treasureToSelect)
    }

    @Test
    fun `SHOULD not change selected treasure WHEN treasure with requested id does not exists in route`() {
        //given
        val model = some<SelectorViewModel>()
        val treasureOriginallySelected = model.getSelectedTreasure()

        //when
        model.selectTreasureById(someInt())

        //then
        assertThat(model.getSelectedTreasure()).isEqualTo(treasureOriginallySelected)
    }

    @Test
    fun `SHOULD return true WHEN id of the given treasure is already among collected`() {
        //given
        val model = some<SelectorViewModel>()
        val collectedTreasure = some<TreasureDescription>()
        model.collect(collectedTreasure)

        //when
        val actual = model.isCollected(collectedTreasure)

        //then
        assertThat(actual).isTrue()
    }

    @Test
    fun `SHOULD return false WHEN id of the given treasure is not already among collected`() {
        //given
        val model = some<SelectorViewModel>()
        val collectedTreasure = some<TreasureDescription>()

        //when
        val actual = model.isCollected(collectedTreasure)

        //then
        assertThat(actual).isFalse()
    }

    @Test
    fun `SHOULD remove treasure id from collected WHEN requesting to uncollect the treasure`() {
        //given
        val model = some<SelectorViewModel>()
        val collectedTreasure = some<TreasureDescription>()
        model.collect(collectedTreasure)

        //when
        model.uncollect(collectedTreasure)

        //then
        assertThat(model.getIdsOfCollectedTreasures()).doesNotContain(collectedTreasure.id)
    }

    @Test
    fun `SHOULD add treasure id to collected WHEN requesting to collect the treasure`() {
        //given
        val model = some<SelectorViewModel>()
        val collectedTreasure = some<TreasureDescription>()

        //when
        model.collect(collectedTreasure)

        //then
        assertThat(model.getIdsOfCollectedTreasures()).contains(collectedTreasure.id)
    }

    @Test
    fun `SHOULD use treasure pretty name WHEN user location is null`() {
        //given
        val model = some<SelectorViewModel>()
        model.initialize(some<Route>(), some<TreasuresProgress>(), false, null, null)
        assertThat(model.userLocation).isNull()
        val treasureDescription = some<TreasureDescription>()

        //when
        val actual = model.generateTreasureDesription(treasureDescription, object : TreasureDescriptionTemplateProvider {
            override fun provide(treasureId: Int, distanceInSteps: Int) = ""
        })

        //then
        assertThat(actual).isEqualTo(treasureDescription.prettyName())
    }

    @Test
    fun `SHOULD use treasure description with number of steps WHEN user location is null`() {
        //given
        val model = some<SelectorViewModel>()
        val treasureDescription = some<TreasureDescription>()
        val expected = someString()
        val calculator = LocationCalculator()

        //when
        val actual = model.generateTreasureDesription(treasureDescription, object : TreasureDescriptionTemplateProvider {
            override fun provide(treasureId: Int, distanceInSteps: Int): String {

                //then
                assertThat(treasureId).isEqualTo(treasureDescription.id)
                assertThat(distanceInSteps).isEqualTo(calculator.distanceInSteps(treasureDescription, model.userLocation!!))
                return expected
            }
        })

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    internal fun `SHOULD replace commemorative file in treasure description`() {
        //given
        val state = Mockito.mock(SavedStateHandle::class.java)
        val selectorViewModel = SelectorViewModel(state)
        val treasure = some<TreasureDescription>()
        val route = some<Route>().copy(treasures = mutableListOf(treasure))
        selectorViewModel.initialize(route, TreasuresProgress(), false, null, null)
        selectorViewModel.selectForCommemorativePhoto(treasure)
        Mockito.reset(state)
        val firstPhoto = Files.createTempFile("test", ".test").toAbsolutePath().toString()
        selectorViewModel.setCommemorativePhotoOnSelectedTreasureDescription(firstPhoto)
        verify(state).set(SelectorViewModel.COMMEMORATIVE_PHOTOS, mutableMapOf(treasure.id to firstPhoto))

        //when
        Mockito.reset(state)
        val secondPhoto = Files.createTempFile("test2", ".test").toAbsolutePath().toString()
        selectorViewModel.setCommemorativePhotoOnSelectedTreasureDescription(secondPhoto)

        //then
        val actualPhoto = selectorViewModel.getCommemorativePhoto(treasure)
        assertThat(actualPhoto).isEqualTo(secondPhoto)
        assertThat(File(firstPhoto).exists()).`as`("the old commemorative photo should be deleted").isFalse()
        verify(state).set(SelectorViewModel.COMMEMORATIVE_PHOTOS, mutableMapOf(treasure.id to secondPhoto))
    }
}