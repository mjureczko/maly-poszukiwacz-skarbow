package pl.marianjureczko.poszukiwacz.activity.searching.n

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
import pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class CustomJustFoundTreasureDescriptionFinderTest {

    @Test
    fun `SHOULD find treasure description by qr code WHEN type is knowledge and qr code is among descriptions`() {
        //given
        val treasureDescription = some<TreasureDescription>()
        val finder = JustFoundTreasureDescriptionFinder(listOf(treasureDescription))
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
        val finder = JustFoundTreasureDescriptionFinder(descriptions)
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
            val someTreasure = some<Treasure>()
            return listOf<Arguments>(
                Arguments.of(
                    "SHOULD return null WHEN description and coordinates are null",
                    someTreasure,
                    null,
                    null,
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN only description is null",
                    someTreasure,
                    someDescription,
                    null,
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN only coordinates is null",
                    someTreasure,
                    null,
                    some<Coordinates>(),
                    0,
                    null
                ),
                Arguments.of(
                    "SHOULD return null WHEN description is far away from coordinates",
                    someTreasure,
                    someDescription,
                    some<Coordinates>(),
                    60,
                    null
                ),
                Arguments.of(
                    "SHOULD return description WHEN description is close to coordinates",
                    someTreasure,
                    someDescription,
                    some<Coordinates>(),
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
        userCoordinates: Coordinates?,
        coordinatesDistance: Int,
        expected: TreasureDescription?
    ) {
        //given
        justFoundTreasure?.let {
            description?.let {
                userCoordinates?.let {
                    BDDMockito.given(locationCalculator.distanceInSteps(description, userCoordinates))
                        .willReturn(coordinatesDistance)
                }
            }
        }
        val finder = JustFoundTreasureDescriptionFinder(listOf(), locationCalculator)
            //justFoundTreasure, description, userCoordinates, locationCalculator)

        //when
        val actual = finder.findTreasureDescription(justFoundTreasure, description, userCoordinates)

        //then
        assertThat(actual).isEqualTo(expected)
    }

}