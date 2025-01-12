package pl.marianjureczko.poszukiwacz.screen.result

import androidx.lifecycle.SavedStateHandle
import com.ocadotechnology.gembus.test.somePositiveInt
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import org.mockito.kotlin.mock
import pl.marianjureczko.poszukiwacz.TestStoragePort
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureType
import java.util.Locale

class ResultViewModelTest {
    @Test
    fun `SHOULD set type not a treasure ie to default WHEN no parameters given`() {
        // Given
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.NOT_A_TREASURE)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type not a treasure WHEN not a treasure given given as the type`() {
        // Given
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(PARAMETER_RESULT_TYPE)).willReturn(ResultType.NOT_A_TREASURE)

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.NOT_A_TREASURE)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type already taken WHEN already taken given as the type`() {
        // Given
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(PARAMETER_RESULT_TYPE)).willReturn(ResultType.ALREADY_TAKEN)

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.ALREADY_TAKEN)
        assertThat(actual.state.value.treasureType).isNull()
    }

    @Test
    fun `SHOULD set type to knowledge WHEN knowledge given as the type but no treasure id`() {
        // Given
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()
        given(savedStateHandle.get<ResultType>(PARAMETER_RESULT_TYPE)).willReturn(ResultType.KNOWLEDGE)

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.KNOWLEDGE)
        assertThat(actual.state.value.treasureType).isEqualTo(TreasureType.KNOWLEDGE)
    }

    @Test
    fun `SHOULD set type to knowledge movie and subtitles path and locales WHEN knowledge given as the type and valid treasure id`() {
        // Given
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()
        val route: Route = storage.routes.values.first()
        given(savedStateHandle.get<ResultType>(PARAMETER_RESULT_TYPE)).willReturn(ResultType.KNOWLEDGE)
        given(savedStateHandle.get<Int>(PARAMETER_TREASURE_ID)).willReturn(route.treasures.first().id)
        given(savedStateHandle.get<String>(PARAMETER_ROUTE_NAME)).willReturn(route.name)

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

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
        val storage = TestStoragePort()
        val savedStateHandle = mock<SavedStateHandle>()
        val route: Route = storage.routes.values.first()
        given(savedStateHandle.get<ResultType>(PARAMETER_RESULT_TYPE)).willReturn(ResultType.GOLD)
        given(savedStateHandle.get<Int>(PARAMETER_TREASURE_ID)).willReturn(route.treasures.first().id)
        given(savedStateHandle.get<String>(PARAMETER_ROUTE_NAME)).willReturn(route.name)
        val amount = somePositiveInt(100)
        given(savedStateHandle.get<Int>(PARAMETER_TREASURE_AMOUNT)).willReturn(amount)

        // When
        val actual = ResultViewModel(savedStateHandle, storage)

        // Then
        assertThat(actual.state.value.resultType).isEqualTo(ResultType.GOLD)
        assertThat(actual.state.value.treasureType).isEqualTo(TreasureType.GOLD)
        assertThat(actual.state.value.amount).isEqualTo(amount)
    }
}