package pl.marianjureczko.poszukiwacz.screen.searching

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.somePositiveInt
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import pl.marianjureczko.poszukiwacz.compass.api.AndroidLocation
import pl.marianjureczko.poszukiwacz.compass.api.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType

class CustomJustFoundTreasureDescriptionFinderTest {

    private val locationCalculator = LocationCalculator()

    @Test
    fun `SHOULD find treasure description by qr code WHEN type is knowledge and qr code is among descriptions`() {
        //given
        val treasureDescription = some<TreasureDescription>()
        val finder = JustFoundTreasureDescriptionFinder(listOf(treasureDescription), locationCalculator)
        val treasure = Treasure(treasureDescription.qrCode!!, somePositiveInt(10), TreasureType.KNOWLEDGE)

        //when
        val actual = finder.findTreasureDescription(treasure)

        //then
        assertThat(actual).isEqualTo(treasureDescription)
    }

    @Test
    fun `SHOULD return null WHEN type is knowledge but qr code not found in treasure descriptions`() {
        //given
        val descriptions = someObjects<TreasureDescription>(2).toList()
        val finder = JustFoundTreasureDescriptionFinder(descriptions, locationCalculator)
        val treasure = Treasure(someString(), somePositiveInt(10), TreasureType.KNOWLEDGE)

        //when
        val actual = finder.findTreasureDescription(treasure)

        //then
        assertThat(actual).isNull()
    }

    @Test
    fun `SHOULD return null WHEN type is knowledge and treasure descriptions list is empty`() {
        //given
        val finder = JustFoundTreasureDescriptionFinder(listOf(), locationCalculator)
        val treasure = Treasure(someString(), somePositiveInt(10), TreasureType.KNOWLEDGE)

        //when
        val actual = finder.findTreasureDescription(treasure)

        //then
        assertThat(actual).isNull()
    }
}

@ExtendWith(MockitoExtension::class)
class ClassicJustFoundTreasureDescriptionFinderTest {

    companion object {
        @JvmStatic
        fun data(): List<Arguments> {
            val someDescription = some<TreasureDescription>()
            val nonKnowledgeTreasure = some<Treasure>().copy(type = TreasureType.GOLD)
            return listOf<Arguments>(
                Arguments.of(
                    "SHOULD return null WHEN description and coordinates are null",
                    nonKnowledgeTreasure,
                    null,
                    null,
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN only description is null",
                    nonKnowledgeTreasure,
                    someDescription,
                    null,
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN only coordinates is null",
                    nonKnowledgeTreasure,
                    null,
                    some<AndroidLocation>(),
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN description is far away from coordinates",
                    nonKnowledgeTreasure,
                    someDescription,
                    some<AndroidLocation>(),
                    60,
                    null
                ),
                Arguments.of(
                    "SHOULD return description WHEN description is close to coordinates",
                    nonKnowledgeTreasure,
                    someDescription,
                    some<AndroidLocation>(),
                    59,
                    someDescription
                ),
            )
        }
    }

    @Mock
    lateinit var locationCalculator: LocationCalculator

    @ParameterizedTest(name = "{0}")
    @MethodSource("data")
    fun findTreasureDescription(
        comment: String,
        justFoundTreasure: Treasure,
        description: TreasureDescription?,
        userCoordinates: AndroidLocation?,
        coordinatesDistance: Int,
        expected: TreasureDescription?
    ) {
        //given
        justFoundTreasure?.let {
            description?.let {
                userCoordinates?.let {
                    BDDMockito.given(locationCalculator.distanceInSteps(any<AndroidLocation>(), eq(userCoordinates)))
                        .willReturn(coordinatesDistance)
                }
            }
        }
        val finder = JustFoundTreasureDescriptionFinder(listOf(), locationCalculator)

        //when
        val actual = finder.findTreasureDescription(justFoundTreasure, description, userCoordinates)

        //then
        assertThat(actual).isEqualTo(expected)
    }

}