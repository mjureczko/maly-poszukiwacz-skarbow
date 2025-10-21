package pl.marianjureczko.poszukiwacz.usecase.badges

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class AddTreasureToAchievementsUCTest {

    val storage = TestAchievementsStoragePort()
    val sut = AddTreasureToAchievementsUC(storage)

    @ParameterizedTest(name = "SHOULD store {0} in quantity from treasure WHEN adding first treasure which is {0}")
    @EnumSource(value = TreasureType::class, names = ["KNOWLEDGE"], mode = EnumSource.Mode.EXCLUDE)
    fun firstTreasure(treasureType: TreasureType) {
        // given
        val routeWithTwoTreasures = some<Route>().copy(
            treasures = listOf(some<TreasureDescription>(), some<TreasureDescription>())
        )
        val treasure = some<Treasure>().copy(type = treasureType)
        val progress = TreasuresProgress()

        // when
        sut(routeWithTwoTreasures, treasure, progress)

        // then
        val actual = storage.load()!!
        assertThat(actual.golds).isEqualTo(if (treasureType == TreasureType.GOLD) treasure.quantity else 0)
        assertThat(actual.rubies).isEqualTo(if (treasureType == TreasureType.RUBY) treasure.quantity else 0)
        assertThat(actual.diamonds).isEqualTo(if (treasureType == TreasureType.DIAMOND) treasure.quantity else 0)
        assertThat(actual.allJewelries()).isEqualTo(treasure.quantity)
        assertThat(actual.treasures).isEqualTo(1)
        assertThat(actual.completedRoutes).isEqualTo(0)
        assertThat(actual.greatestNumberOfTreasuresOnRoute).isEqualTo(0)
    }

    @Test
    fun `SHOULD store one knowledge treasure WHEN adding first knowledge treasure`() {
        // given
        val routeWithTwoTreasures = some<Route>().copy(
            treasures = listOf(some<TreasureDescription>(), some<TreasureDescription>())
        )
        val knowledgeTreasure = some<Treasure>().copy(type = TreasureType.KNOWLEDGE, quantity = 1)
        val progress = TreasuresProgress()

        // when
        sut(routeWithTwoTreasures, knowledgeTreasure, progress)

        // then
        val actual = storage.load()!!
        assertThat(actual.golds).isEqualTo(0)
        assertThat(actual.rubies).isEqualTo(0)
        assertThat(actual.diamonds).isEqualTo(0)
        assertThat(actual.knowledge).isEqualTo(1)
        assertThat(actual.allJewelries()).isEqualTo(0)
        assertThat(actual.treasures).isEqualTo(1)
        assertThat(actual.completedRoutes).isEqualTo(0)
        assertThat(actual.greatestNumberOfTreasuresOnRoute).isEqualTo(0)
    }

    @Test
    fun `SHOULD throw exception WHEN ading knowledge treasure with quantity greater than 1`() {
        // given
        val routeWithTwoTreasures = some<Route>().copy(
            treasures = listOf(some<TreasureDescription>(), some<TreasureDescription>())
        )
        val knowledgeTreasure = some<Treasure>().copy(type = TreasureType.KNOWLEDGE, quantity = 2)
        val progress = TreasuresProgress()

        // when - then
        assertThatThrownBy {
            sut(routeWithTwoTreasures, knowledgeTreasure, progress)
        }.isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("Knowledge treasure quantity must be 1")
    }

    @Test
    fun `SHOULD store all kinds of treasures and update all remaining achievements WHNE adding all treasures from route`() {
        // given
        val routeWithThreeTreasures = some<Route>().copy(
            treasures = listOf(some<TreasureDescription>(), some<TreasureDescription>(), some<TreasureDescription>())
        )
        val gold = some<Treasure>().copy(type = TreasureType.GOLD)
        val ruby = some<Treasure>().copy(type = TreasureType.RUBY)
        val diamond = some<Treasure>().copy(type = TreasureType.DIAMOND)
        var progress = TreasuresProgress()

        // when
        progress = progress.collect(gold, null)
        sut(routeWithThreeTreasures, gold, progress)
        progress = progress.collect(ruby, null)
        sut(routeWithThreeTreasures, ruby, progress)
        progress = progress.collect(diamond, null)
        sut(routeWithThreeTreasures, diamond, progress)

        // then
        val actual = storage.load()!!
        assertThat(actual.golds).isEqualTo(gold.quantity)
        assertThat(actual.rubies).isEqualTo(ruby.quantity)
        assertThat(actual.diamonds).isEqualTo(diamond.quantity)
        assertThat(actual.allJewelries()).isEqualTo(gold.quantity + ruby.quantity + diamond.quantity)
        assertThat(actual.treasures).isEqualTo(3)
        assertThat(actual.completedRoutes).isEqualTo(1)
        assertThat(actual.greatestNumberOfTreasuresOnRoute).isEqualTo(3)
    }

    @ParameterizedTest(name = "SHOULD add {0} in given quantity WHEN storing multiple {0} treasures")
    @EnumSource(value = TreasureType::class, names = ["KNOWLEDGE"], mode = EnumSource.Mode.EXCLUDE)
    fun addingTreasures(treasureType: TreasureType) {
        // given
        val routeWithThreeTreasures = some<Route>().copy(
            treasures = listOf(some<TreasureDescription>(), some<TreasureDescription>(), some<TreasureDescription>())
        )
        val treasure1 = some<Treasure>().copy(type = treasureType)
        val treasure2 = some<Treasure>().copy(type = treasureType)
        val progress = TreasuresProgress()

        // when
        sut(routeWithThreeTreasures, treasure1, progress)
        sut(routeWithThreeTreasures, treasure2, progress)

        //then
        val actual = storage.load()!!
        val expected = treasure1.quantity + treasure2.quantity
        assertThat(actual.golds).isEqualTo(if (treasureType == TreasureType.GOLD) expected else 0)
    }
}