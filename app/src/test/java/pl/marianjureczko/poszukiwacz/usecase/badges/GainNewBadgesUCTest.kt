package pl.marianjureczko.poszukiwacz.usecase.badges

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

class GainNewBadgesUCTest {

    private val sut = GainNewBadgesUC()

    @Test
    fun `SHOULD return empty list WHEN nothing in achievements`() {
        //given
        val emptyAchievements = Achievements()

        //when
        val actual = sut(emptyAchievements)

        //then
        assertThat(actual).isEmpty()
    }

    @Test
    fun `SHOULD return empty list WHEN everything in progress below 1st badge threshold`() {
        //given
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
        val actual = sut(lowAchievements)

        //then
        assertThat(actual).isEmpty()
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("goldCases")
    fun gainGoldBadges(comment: String, achievements: Achievements, expectedBadges: List<Badge>) {
        //given

        //when
        val actual = sut(achievements)

        //then
        assertThat(actual.filter { it.type == BadgeType.GoldHunter })
            .hasSize(expectedBadges.size)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactlyElementsOf(expectedBadges)
    }

    companion object {
        @JvmStatic
        fun goldCases(): Stream<Arguments> = Stream.of(
            Arguments.of(
                "SHOULD return first gold badge WHEN gold is 50 and no gold badges",
                Achievements().copy(golds = 50),
                listOf(Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 50))
            ),
            Arguments.of(
                "SHOULD return empty list WHEN gold is 50 but gold badge already granted",
                Achievements().copy(
                    golds = 50,
                    badges = listOf(Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 50))
                ),
                emptyList<Badge>()
            ),
            Arguments.of(
                "SHOULD return 3rd gold badge WHEN gold is 150 and only 2 gold badges achieved",
                Achievements().copy(
                    golds = 150,
                    badges = listOf(
                        Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 51),
                        Badge(type = BadgeType.GoldHunter, level = 2, achievementValue = 102)
                    )
                ),
                listOf(Badge(type = BadgeType.GoldHunter, level = 3, achievementValue = 150))
            ),
            Arguments.of(
                "SHOULD empty list WHEN gold is 150 and 3 gold badges already exist",
                Achievements().copy(
                    golds = 150,
                    badges = listOf(
                        Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 51),
                        Badge(type = BadgeType.GoldHunter, level = 2, achievementValue = 102),
                        Badge(type = BadgeType.GoldHunter, level = 3, achievementValue = 150)
                    )
                ),
                emptyList<Badge>()
            )
        )
    }

    @Test
    fun `SHOULD return all 4 jewellery badges WHEN each kind above already granted brands`() {
        //given
        val golds = 105
        val rubies = 100
        val diamonds = 55
        val achievements = Achievements().copy(
            golds = golds,
            rubies = rubies,
            diamonds = diamonds,
            badges = listOf(
                Badge(type = BadgeType.GoldHunter, level = 1, achievementValue = 51),
                Badge(type = BadgeType.RubyCollector, level = 1, achievementValue = 54)
            )
        )

        //when
        val actual = sut(achievements)

        //then
        assertThat(actual)
            .hasSize(4)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactlyElementsOf(
                listOf(
                    Badge(type = BadgeType.GoldHunter, level = 2, achievementValue = golds),
                    Badge(type = BadgeType.RubyCollector, level = 2, achievementValue = rubies),
                    Badge(type = BadgeType.DiamondExplorer, level = 1, achievementValue = diamonds),
                    Badge(type = BadgeType.Treasurer, level = 1, achievementValue = golds + rubies + diamonds),
                )
            )
    }

    @Test
    fun `SHOULD return all but simple jewellery badges WHEN each remaining kind above already granted brands`() {
        //given
        val jewelryQuantity = 34
        val knowledge = 10
        val completedRoutes = 5
        val longestRoute = 3
        val treasures = 10
        val achievements = Achievements().copy(
            golds = jewelryQuantity,
            rubies = jewelryQuantity,
            diamonds = jewelryQuantity,
            treasures = treasures,
            completedRoutes = completedRoutes,
            knowledge = knowledge,
            greatestNumberOfTreasuresOnRoute = longestRoute,
            badges = listOf()
        )

        //when
        val actual = sut(achievements)

        //then
        assertThat(actual)
            .hasSize(5)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactlyInAnyOrderElementsOf(
                listOf(
                    Badge(type = BadgeType.Treasurer, level = 1, achievementValue = jewelryQuantity * 3),
                    Badge(type = BadgeType.TreasureSeeker, level = 1, achievementValue = treasures),
                    Badge(type = BadgeType.KnowledgeHero, level = 1, achievementValue = knowledge),
                    Badge(type = BadgeType.EnduringTraveler, level = 1, achievementValue = completedRoutes),
                    Badge(type = BadgeType.Pathfinder, level = 1, achievementValue = longestRoute),
                )
            )
    }

    @Test
    fun `SHOULD grand 2nd pathfinder badge WHEN currently achieved longest route is longer than the one from 1st badge`() {
        //given
        val routeLength = 3
        val achievements = Achievements().copy(
            greatestNumberOfTreasuresOnRoute = routeLength,
            badges = listOf(Badge(type = BadgeType.Pathfinder, level = 1, achievementValue = 2))
        )

        //when
        val actual = sut(achievements)

        //then
        assertThat(actual)
            .hasSize(1)
            .usingRecursiveFieldByFieldElementComparatorIgnoringFields(Badge::timestamp.name)
            .containsExactlyInAnyOrderElementsOf(
                listOf(
                    Badge(type = BadgeType.Pathfinder, level = 2, achievementValue = routeLength),
                )
            )
    }

    @Test
    fun `SHOULD not grand 2nd pathfinder WHEN currently finished route is of the same length as the badgelevel`() {
        //given
        val achievements = Achievements().copy(
            treasures = 4,
            completedRoutes = 2,
            greatestNumberOfTreasuresOnRoute = 2,
            badges = listOf(
                Badge(type = BadgeType.Pathfinder, level = 1, achievementValue = 2),
            )
        )

        //when
        val actual = sut(achievements)

        //then
        assertThat(actual).isEmpty()
    }
}