package pl.marianjureczko.poszukiwacz.activity.searching

import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

//import pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates
//import pl.marianjureczko.poszukiwacz.model.Treasure
//import pl.marianjureczko.poszukiwacz.model.TreasureDescription

@ExtendWith(MockitoExtension::class)
class JustFoundFinderTest {
//    companion object {
//        @JvmStatic
//        fun data(): List<Arguments> {
//            val someDescription = some<TreasureDescription>()
//            val someTreasure = some<Treasure>()
//            return listOf<Arguments>(
//                Arguments.of(
//                    "SHOULD return null WHEN treasure is null",
//                    null,
//                    someDescription,
//                    some<pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates>(),
//                    0,
//                    null
//                ),
//                Arguments.of(
//                    "SHOULD return null WHEN description and coordinates are null",
//                    someTreasure,
//                    null,
//                    null,
//                    0,
//                    null
//                ),
//                Arguments.of(
//                    "SHOULD return null WHEN only description is null",
//                    someTreasure,
//                    someDescription,
//                    null,
//                    0,
//                    null
//                ),
//                Arguments.of(
//                    "SHOULD return null WHEN only coordinates is null",
//                    someTreasure,
//                    null,
//                    some<pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates>(),
//                    0,
//                    null
//                ),
//                Arguments.of(
//                    "SHOULD return null WHEN description is far away from coordinates",
//                    someTreasure,
//                    someDescription,
//                    some<pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates>(),
//                    60,
//                    null
//                ),
//                Arguments.of(
//                    "SHOULD return description WHEN description is close to coordinates",
//                    someTreasure,
//                    someDescription,
//                    some<pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates>(),
//                    59,
//                    someDescription
//                ),
//            )
//        }
//    }
//
//    @Mock
//    lateinit var locationCalculator: pl.marianjureczko.poszukiwacz.activity.searching.LocationCalculator
//
//    @ParameterizedTest(name = "{0}")
//    @MethodSource("data")
//    fun findTreasureDescription(
//        comment: String,
//        justFoundTreasure: Treasure?,
//        description: TreasureDescription?,
//        userCoordinates: pl.marianjureczko.poszukiwacz.activity.treasureselector.Coordinates?,
//        coordinatesDistance: Int,
//        expected: TreasureDescription?
//    ) {
//        //given
//        justFoundTreasure?.let {
//            description?.let {
//                userCoordinates?.let {
//                    BDDMockito.given(locationCalculator.distanceInSteps(description, userCoordinates))
//                        .willReturn(coordinatesDistance)
//                }
//            }
//        }
//        val finder = pl.marianjureczko.poszukiwacz.activity.searching.JustFoundFinder(
//            justFoundTreasure,
//            description,
//            userCoordinates,
//            locationCalculator
//        )
//
//        //when
//        val actual = finder.findTreasureDescription()
//
//        //then
//        assertThat(actual).isEqualTo(expected)
//    }
}