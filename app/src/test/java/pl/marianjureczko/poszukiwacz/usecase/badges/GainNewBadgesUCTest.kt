package pl.marianjureczko.poszukiwacz.usecase.badges

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class GainNewBadgesUCTest {
    @Test
    fun `SHOULD return empty list WHEN nothing in achievements`() {
        //given
        val sut = GainNewBadgesUC()
        val emptyAchievements = Achievements()

        //when
        val actual = sut.invoke(emptyAchievements)

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `SHOULD return empty list WHEN everything in progress below 1st badge threshold`() {
        //given
        val sut = GainNewBadgesUC()
        val lowAchievements = Achievements().copy(
            golds = 49,
            rubies = 49,
            diamonds = 1,
            knowledge = 4,
            treasures = 9,
            completedRoutes = 4,
            greatestNumberOfTreasuresOnRoute = 0
        )

        //when
        val actual = sut.invoke(lowAchievements)

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `SHOULD return first gold badge WHEN gold is 50 and no gold badges in achievements`() {
        //given
        val sut = GainNewBadgesUC()
        val thresholdForFirstBadge = 50
        val achievements = Achievements().copy(golds = thresholdForFirstBadge)

        //when
        val actual = sut.invoke(achievements)

        //then
        assertThat(actual)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactly(Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = thresholdForFirstBadge))
    }

    @Test
    fun `SHOULD return empty list WHEN gold is 50 and gold badges is already granted`() {
        //given
        val sut = GainNewBadgesUC()
        val thresholdForFirstBadge = 50
        val achievements = Achievements().copy(
            golds = thresholdForFirstBadge,
            badges = listOf(Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = thresholdForFirstBadge)),
        )

        //when
        val actual = sut.invoke(achievements)

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `SHOULD return 3rd gold badge WHEN gold is 150 and only 2 gold badges in achievements`() {
        //given
        val sut = GainNewBadgesUC()
        val thresholdFor3rdtBadge = 150
        val achievements = Achievements().copy(
            golds = thresholdFor3rdtBadge,
            badges = listOf(
                Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 51),
                Badge(type = BadgeType.GoldHunter, level = 2, achievementValue = 102),
            ),
        )

        //when
        val actual = sut.invoke(achievements)

        //then
        assertThat(actual)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactly(Badge(type = BadgeType.GoldHunter, level = 3, achievementValue = thresholdFor3rdtBadge))
    }

    @Test
    fun `SHOULD return empty list WHEN gold is 150 and 3 gold badges in achievements`() {
        //given
        val sut = GainNewBadgesUC()
        val thresholdFor3rdtBadge = 50
        val achievements = Achievements().copy(
            golds = thresholdFor3rdtBadge,
            badges = listOf(
                Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 51),
                Badge(type = BadgeType.GoldHunter, level = 2, achievementValue = 102),
                Badge(type = BadgeType.GoldHunter, level = 3, achievementValue = 150),
            ),
        )

        //when
        val actual = sut.invoke(achievements)

        //then
        assertThat(actual).isEmpty()
    }
}