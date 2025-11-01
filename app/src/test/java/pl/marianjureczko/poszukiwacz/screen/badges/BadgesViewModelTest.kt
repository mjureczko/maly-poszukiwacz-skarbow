package pl.marianjureczko.poszukiwacz.screen.badges

import com.ocadotechnology.gembus.test.some
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
        assert(actual.golds == achievements.golds)
        assert(actual.diamonds == achievements.diamonds)
        assert(actual.rubies == achievements.rubies)
        assert(actual.knowledge == achievements.knowledge)
        assert(actual.treasures == achievements.treasures)
        assert(actual.completedRoutes == achievements.completedRoutes)
        assert(actual.greatestNumberOfTreasuresOnRoute == achievements.greatestNumberOfTreasuresOnRoute)
        assert(actual.badges == achievements.badges)
    }
}