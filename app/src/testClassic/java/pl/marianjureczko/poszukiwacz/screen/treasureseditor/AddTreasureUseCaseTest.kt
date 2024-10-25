package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Offset

import org.junit.jupiter.api.Test
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class AddTreasureUseCaseTest {

    private val offset = Offset.offset(0.001)

    @Test
    fun shouldAddTreasureDescription_whenAddingToEmptyRoute() {
        //given
        val storage = TestStoragePort()
        val useCase = AddTreasureUseCase(storage)
        val coordinates = some<Coordinates>()
        val route = Route(someString())

        //when
        val actualRoute = useCase(route, coordinates)
        val persistedRoute = storage.routes[route.name]

        //then
        assertThat(actualRoute!!.treasures).hasSize(1)
        assertThat(actualRoute.treasures[0].latitude).isEqualTo(coordinates.latitude, offset)
        assertThat(actualRoute.treasures[0].longitude).isEqualTo(coordinates.longitude, offset)

        assertThat(persistedRoute).isNotNull
        assertThat(persistedRoute!!.treasures).hasSize(1)
        assertThat(persistedRoute.treasures[0].latitude).isEqualTo(coordinates.latitude, offset)
        assertThat(persistedRoute.treasures[0].longitude).isEqualTo(coordinates.longitude, offset)
    }
}