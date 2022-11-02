package pl.marianjureczko.poszukiwacz.activity.map

import com.mapbox.geojson.Point
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someInt
import org.assertj.core.api.Assertions.assertThat

import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

internal class LocationHelperKtTest {

    @Test
    fun findCenterForSingleTreasure() {
        //given
        val treasure = some<TreasureDescription>()
        val route = some<Route>().copy(treasures = mutableListOf(treasure))

        //when
        val actual = LocationHelper(route).center()

        //then
        assertThat(actual).isEqualTo(Point.fromLngLat(treasure.longitude, treasure.latitude))
    }

    @Test
    fun findCenterForNoTreasure() {
        //given
        val route = some<Route>().copy(treasures = mutableListOf())

        //when
        val actual = LocationHelper(route).center()

        //then
        assertThat(actual).isEqualTo(Point.fromLngLat(0.0, 0.0))
    }

    @Test
    fun findCenterForManyTreasures() {
        //given
        val min = some<TreasureDescription>().copy(latitude = -0.1, longitude = -20.1)
        val max = some<TreasureDescription>().copy(latitude = 11.1, longitude = 21.1)
        val other = some<TreasureDescription>().copy(latitude = someInt(0, 10).toDouble(), longitude = someInt(0, 10).toDouble())
        val route = some<Route>().copy(treasures = mutableListOf(min, max, other))

        //when
        val actual = LocationHelper(route).center()

        //then
        val long = (min.longitude + max.longitude) / 2
        val lat = (min.latitude + max.latitude) / 2
        assertThat(actual).isEqualTo(Point.fromLngLat(long, lat))
    }

    @Test
    internal fun findSouthwest() {
        //given
        val min = some<TreasureDescription>().copy(latitude = -0.1, longitude = -20.1)
        val max = some<TreasureDescription>().copy(latitude = 11.1, longitude = 21.1)
        val route = some<Route>().copy(treasures = mutableListOf(min, max))

        //when
        val actual = LocationHelper(route).southwest()

        //then
        assertThat(actual).isEqualTo(Point.fromLngLat(min.longitude, min.latitude))
    }

    @Test
    internal fun findNorteast() {
        //given
        val min = some<TreasureDescription>().copy(latitude = -0.1, longitude = -20.1)
        val max = some<TreasureDescription>().copy(latitude = 11.1, longitude = 21.1)
        val route = some<Route>().copy(treasures = mutableListOf(min, max))

        //when
        val actual = LocationHelper(route).northeast()

        //then
        assertThat(actual).isEqualTo(Point.fromLngLat(max.longitude, max.latitude))
    }
}