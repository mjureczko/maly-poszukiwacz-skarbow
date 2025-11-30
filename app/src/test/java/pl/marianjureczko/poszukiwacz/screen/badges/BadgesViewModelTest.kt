package pl.marianjureczko.poszukiwacz.screen.badges

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.given
import pl.marianjureczko.poszukiwacz.usecase.badges.Achievements
import pl.marianjureczko.poszukiwacz.usecase.badges.AchievementsStoragePort

class BadgesViewModelTest {

    @Test
    fun `SHOULD load data from achievements port to state`() {
        //given
        val achievements = some<Achievements>()
        val port: AchievementsStoragePort = mock()
        given(port.load()).willReturn(achievements)

        //when
        val sut = BadgesViewModel(port)
        val actual = sut.state.value

        //then
        assertThat(actual.golds).isEqualTo(achievements.golds)
        assertThat(actual.diamonds).isEqualTo(achievements.diamonds)
        assertThat(actual.rubies).isEqualTo(achievements.rubies)
        assertThat(actual.knowledge).isEqualTo(achievements.knowledge)
        assertThat(actual.treasures).isEqualTo(achievements.treasures)
        assertThat(actual.completedRoutes).isEqualTo(achievements.completedRoutes)
        assertThat(actual.greatestNumberOfTreasuresOnRoute).isEqualTo(achievements.greatestNumberOfTreasuresOnRoute)
        assertThat(actual.badges).containsExactlyInAnyOrderElementsOf(achievements.badges)
    }
}