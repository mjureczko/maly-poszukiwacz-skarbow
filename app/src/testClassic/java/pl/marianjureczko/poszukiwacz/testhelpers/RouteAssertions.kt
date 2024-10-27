package pl.marianjureczko.poszukiwacz.testhelpers

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates

val offset: Offset<Double> = Offset.offset(0.001)

fun assertRouteContainsTreasureWith(actualRoute: Route, expectedId: Int, expectedCoordinates: Coordinates) {
    val actualTreasure = actualRoute.treasures.find { it.id == expectedId }
    assertThat(actualTreasure).isNotNull()
    assertThat(actualTreasure!!.latitude).isEqualTo(expectedCoordinates.latitude, offset)
    assertThat(actualTreasure.longitude).isEqualTo(expectedCoordinates.longitude, offset)
}

fun assertRouteDoesNotContainTresureWithId(actualRoute: Route, expectedId: Int) {
    val find = actualRoute.treasures.find { treasureDescription -> treasureDescription.id == expectedId }
    assertThat(find).isNull()
}