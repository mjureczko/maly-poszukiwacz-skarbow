package pl.marianjureczko.poszukiwacz.screen.searching

import android.media.MediaPlayer
import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.model.HunterPath
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.usecase.LocationHolder

class SharedStateTest {
    @Test
    fun shouldReturnTreasureDescriptionFromRoute_whenOneIsSelectedInProgress() {
        //given
        val route = some<Route>()
        val selectedTreasureDescription = route.treasures.last()
        val state = SharedState(
            mediaPlayer = mock(MediaPlayer::class.java),
            route = route,
            treasuresProgress = some<TreasuresProgress>().copy(selectedTreasureDescriptionId = selectedTreasureDescription.id),
            currentLocation = LocationHolder(),
            stepsToTreasure = null,
            hunterPath = HunterPath(route.name),
        )

        //when
        val actual = state.selectedTreasureDescription()

        //then
        assertThat(actual).isEqualTo(selectedTreasureDescription)
    }
}