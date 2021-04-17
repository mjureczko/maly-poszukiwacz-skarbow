package pl.marianjureczko.poszukiwacz.model

import org.junit.Assert.*
import org.junit.Test
import pl.marianjureczko.poszukiwacz.model.TreasureType

class TreasureBagTest {

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
        val bag = TreasureBag()

        //when
        bag.collect(gold7)
        bag.collect(gold9)

        //then
        assertEquals(gold7.quantity + gold9.quantity, bag.golds)
        assertEquals(0, bag.diamonds)
        assertEquals(0, bag.rubies)
    }

    @Test
    fun addDiamondsToBag() {
        //given
        val bag = TreasureBag()

        //when
        bag.collect(diamond17)
        bag.collect(diamond18)

        //then
        assertEquals(diamond17.quantity + diamond18.quantity, bag.diamonds)
        assertEquals(0, bag.golds)
        assertEquals(0, bag.rubies)
    }

    @Test
    fun addRubiesToBag() {
        //given
        val bag = TreasureBag()

        //when
        bag.collect(ruby27)
        bag.collect(ruby98)

        //then
        assertEquals(ruby27.quantity + ruby98.quantity, bag.rubies)
        assertEquals(0, bag.golds)
        assertEquals(0, bag.diamonds)
    }

    @Test
    fun detectAlreadyCollectedTreasures() {
        //given
        val bag = TreasureBag()
        bag.collect(gold9)

        //then
        assertFalse(bag.contains(gold7))
        assertTrue(bag.contains(gold9))
    }
}