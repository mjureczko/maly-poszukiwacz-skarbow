package pl.marianjureczko.poszukiwacz.screen.result

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.some
import com.ocadotechnology.gembus.test.someObjects
import com.ocadotechnology.gembus.test.somePositiveInt
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.usecase.badges.AddTreasureToAchievementsUC
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge
import java.util.Locale

class ResultViewModelTest {

    private val addTreasureToAchievementsUC = mock<AddTreasureToAchievementsUC>()
    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `SHOULD load route and progress WHEN isJustFound is true and valid treasure id given`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        val route = some<Route>()
        storage.save(route)
        val progress = some<TreasuresProgress>().copy(routeName = route.name)
        storage.save(progress)
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.GOLD)
        given(savedStateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)).willReturn(route.name)
        given(savedStateHandle.get<Boolean>(Screens.Results.PARAMETER_IS_JUST_FOUND)).willReturn(true)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.route).isEqualTo(route)
        assertThat(actual.state.value.progress).isEqualTo(progress)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `SHOULD set badges to show and flag show badges WHEN achievements uc returns badges`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        val route = some<Route>()
        storage.save(route)
        val progress = some<TreasuresProgress>().copy(routeName = route.name)
        storage.save(progress)
        val amount = somePositiveInt(100)
        val treasure = Treasure("_", amount, TreasureType.KNOWLEDGE)
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.KNOWLEDGE)
        given(savedStateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)).willReturn(route.name)
        given(savedStateHandle.get<Boolean>(Screens.Results.PARAMETER_IS_JUST_FOUND)).willReturn(true)
        given(savedStateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_AMOUNT)).willReturn(amount)
        val expectedBadges: List<Badge> = someObjects<Badge>(2).toList()
        given(addTreasureToAchievementsUC(route, treasure, progress)).willReturn(expectedBadges)

        // When
        testScope.runTest {
            val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)
            advanceUntilIdle()

            // Then
            assertThat(actual.state.value.badgesToShow).isEqualTo(expectedBadges)
            assertThat(actual.state.value.isBadgesVisible).isTrue()
        }
    }

    @Test
    fun `SHOULD set type not a treasure ie to default WHEN no parameters given`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.NOT_A_TREASURE)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type not a treasure WHEN not a treasure given given as the type`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.NOT_A_TREASURE)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.NOT_A_TREASURE)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type already taken WHEN already taken given as the type`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.ALREADY_TAKEN)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.ALREADY_TAKEN)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type to knowledge WHEN knowledge given as the type but no treasure id`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.KNOWLEDGE)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.KNOWLEDGE)
        assertThat(actual.state.value.treasureType).isEqualTo(TreasureType.KNOWLEDGE)
    }

    @Test
    fun `SHOULD set type to knowledge movie and subtitles path and locales WHEN knowledge given as the type and valid treasure id`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        val route: Route = storage.routes.values.first()
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.KNOWLEDGE)
        given(savedStateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_ID)).willReturn(route.treasures.first().id)
        given(savedStateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)).willReturn(route.name)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.KNOWLEDGE)
        assertThat(actual.state.value.treasureType).isEqualTo(TreasureType.KNOWLEDGE)
        assertThat(actual.state.value.moviePath).isEqualTo(route.treasures.first().movieFileName)
        assertThat(actual.state.value.subtitlesPath).isEqualTo(route.treasures.first().subtitlesFileName)
        assertThat(actual.state.value.localesWithSubtitles).isEqualTo(!"pl".equals(Locale.getDefault().language, true))
    }

    @Test
    fun `SHOULD set amount and type to gold WHEN gold given as the type and valid treasure id`() {
        // Given
        val storage = TestStoragePort(mock<Context>())
        val savedStateHandle = mock<SavedStateHandle>()
        val route: Route = storage.routes.values.first()
        given(savedStateHandle.get<ResultType>(Screens.Results.PARAMETER_RESULT_TYPE)).willReturn(ResultType.GOLD)
        given(savedStateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_ID)).willReturn(route.treasures.first().id)
        given(savedStateHandle.get<String>(Screens.Results.PARAMETER_ROUTE_NAME)).willReturn(route.name)
        val amount = somePositiveInt(100)
        given(savedStateHandle.get<Int>(Screens.Results.PARAMETER_TREASURE_AMOUNT)).willReturn(amount)

        // When
        val actual = ResultViewModel(savedStateHandle, storage, addTreasureToAchievementsUC, testDispatcher)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.GOLD)
        assertThat(actual.state.value.treasureType).isEqualTo(TreasureType.GOLD)
        assertThat(actual.state.value.amount).isEqualTo(amount)
    }
}