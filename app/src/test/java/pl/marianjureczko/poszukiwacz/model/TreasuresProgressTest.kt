package pl.marianjureczko.poszukiwacz.model

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.shared.port.XmlHelper

class TreasuresProgressTest {

    val gold7 = Treasure(
        "a",
        7,
        TreasureType.GOLD
    )
    val gold9 = Treasure(
        "b",
        9,
        TreasureType.GOLD
    )
    val diamond17 = Treasure(
        "c",
        17,
        TreasureType.DIAMOND
    )
    val diamond18 = Treasure(
        "d",
        18,
        TreasureType.DIAMOND
    )
    val ruby27 = Treasure(
        "e",
        27,
        TreasureType.RUBY
    )
    val ruby98 = Treasure(
        "f",
        98,
        TreasureType.RUBY
    )

    @Test
    fun addGoldToBag() {
        //given
        val bag = TreasuresProgress(someString(), some<TreasureDescription>())

        //when
        bag.collect(gold7, null)
        bag.collect(gold9, null)

        //then
        assertEquals(gold7.quantity + gold9.quantity, bag.golds)
        assertEquals(0, bag.diamonds)
        assertEquals(0, bag.rubies)
    }

    @Test
    fun addDiamondsToBag() {
        //given
        val bag = TreasuresProgress(someString(), some<TreasureDescription>())

        //when
        bag.collect(diamond17, null)
        bag.collect(diamond18, null)

        //then
        assertEquals(diamond17.quantity + diamond18.quantity, bag.diamonds)
        assertEquals(0, bag.golds)
        assertEquals(0, bag.rubies)
    }

    @Test
    fun addRubiesToBag() {
        //given
        val bag = TreasuresProgress(someString(), some<TreasureDescription>())

        //when
        bag.collect(ruby27, null)
        bag.collect(ruby98, null)

        //then
        assertEquals(ruby27.quantity + ruby98.quantity, bag.rubies)
        assertEquals(0, bag.golds)
        assertEquals(0, bag.diamonds)
    }

    @Test
    fun detectAlreadyCollectedTreasures() {
        //given
        val bag = TreasuresProgress(someString(), some<TreasureDescription>())
        bag.collect(gold9, null)

        //then
        assertFalse(bag.contains(gold7))
        assertTrue(bag.contains(gold9))
    }

    @Test
    fun shouldBeSerializableToXml() {
        //given
        val xmlHelper = XmlHelper()
        val routeName = someString()
        val treasuresProgress = TreasuresProgress(routeName, some<TreasureDescription>())
        val treasure = some<Treasure>().copy(type = TreasureType.DIAMOND)
        val description = some<TreasureDescription>()
        treasuresProgress.collect(treasure, description)

        //when
        val xml = xmlHelper.writeToString(treasuresProgress)
        val actual = xmlHelper.loadFromString<TreasuresProgress>(xml)

        //then
        assertThat(actual.routeName).isEqualTo(routeName)
        assertThat(actual.contains(treasure)).isTrue()
        assertThat(actual.diamonds).isEqualTo(treasure.quantity)
        assertThat(actual.collectedTreasuresDescriptionId).containsExactly(description.id)
    }

    @Test
    fun `SHOULD add treasure description to collected WHEN has not been yet collected`() {
        //given
        val bag = some<TreasuresProgress>()
        val expected = someInt()

        //when
        val result = bag.toggleTreasureDescriptionCollected(expected)

        //then
        assertTrue(result.collectedTreasuresDescriptionId.contains(expected))
    }

    @Test
    fun `SHOULD remove treasure description id from collected WHEN was already collected`() {
        //given
        val bag = some<TreasuresProgress>()

        //when
        val result = bag.toggleTreasureDescriptionCollected(bag.collectedTreasuresDescriptionId.first())

        //then
        assertFalse(result.collectedTreasuresDescriptionId.contains(bag.collectedTreasuresDescriptionId.first()))
    }
}