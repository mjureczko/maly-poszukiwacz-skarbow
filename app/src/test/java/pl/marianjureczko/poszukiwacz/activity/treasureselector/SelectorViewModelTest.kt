package pl.marianjureczko.poszukiwacz.activity.treasureselector

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import com.ocadotechnology.gembus.test.someLong
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

internal class SelectorViewModelTest {

    @Test
    fun `SHOULD select treasure in progress by id WHEN treasure with requested id exists in route`() {
        //given
        val model = some<SelectorViewModel>()
        val treasureToSelect = model.route.treasures[0]

        //when
        model.selectTreasureById(treasureToSelect.id)

        //then
        assertThat(model.progress.selectedTreasure).isEqualTo(treasureToSelect)
    }

    @Test
    fun `SHOULD not change selected treasure WHEN treasure with requested id does not exists in route`() {
        //given
        val model = some<SelectorViewModel>()
        val treasureOriginallySelected = model.progress.selectedTreasure

        //when
        model.selectTreasureById(someInt())

        //then
        assertThat(model.progress.selectedTreasure).isEqualTo(treasureOriginallySelected)
    }


}