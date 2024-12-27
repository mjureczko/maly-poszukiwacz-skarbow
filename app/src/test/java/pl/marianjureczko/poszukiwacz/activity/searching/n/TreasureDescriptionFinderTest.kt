package pl.marianjureczko.poszukiwacz.activity.searching.n

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.somePositiveInt
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureDescriptionArranger
import pl.marianjureczko.poszukiwacz.model.TreasureType

class TreasureDescriptionFinderTest {

    @Test
    fun `SHOULD find treasure description by qr code WHEN type is knowledge and qr code is among descriptions`() {
        //given
        val treasureDescription = some<TreasureDescription>()
        val finder = TreasureDescriptionFinder(listOf(treasureDescription))
        val treasure = Treasure(someString(), somePositiveInt(10), TreasureType.KNOWLEDGE)

        //when
        val actual = finder.findTreasureDescription(treasureDescription.qrCode!!, treasure)

        //then
        assertThat(actual).isEqualTo(treasureDescription)
    }

    @Test
    fun `SHOULD return null WHEN type is knowledge but qr code not found in treasure descriptions`() {
        //given
        val descriptions = someObjects<TreasureDescription>(2).toList()
        val finder = TreasureDescriptionFinder(descriptions)
        val treasure = Treasure(someString(), somePositiveInt(10), TreasureType.KNOWLEDGE)

        //when
        val actual = finder.findTreasureDescription(TreasureDescriptionArranger.validQrCode("k"), treasure)

        //then
        assertThat(actual).isNull()

    }
}