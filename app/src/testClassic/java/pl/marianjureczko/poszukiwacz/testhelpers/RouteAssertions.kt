package pl.marianjureczko.poszukiwacz.testhelpers

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.usecase.AndroidLocation

val offset: Offset<Double> = Offset.offset(0.001)

fun assertRouteContainsTreasureWith(actualRoute: Route, expectedId: Int, expectedLocation: AndroidLocation) {
    val actualTreasure = actualRoute.treasures.find { it.id == expectedId }
    assertThat(actualTreasure).isNotNull()
    assertThat(actualTreasure!!.latitude).isEqualTo(expectedLocation.latitude, offset)
    assertThat(actualTreasure.longitude).isEqualTo(expectedLocation.longitude, offset)
}

fun assertRouteDoesNotContainTresureWithId(actualRoute: Route, expectedId: Int) {
    val find = actualRoute.treasures.find { treasureDescription -> treasureDescription.id == expectedId }
    assertThat(find).isNull()
}