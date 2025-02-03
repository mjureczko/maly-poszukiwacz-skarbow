package pl.marianjureczko.poszukiwacz.activity.facebook

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.marianjureczko.poszukiwacz.screen.facebook.treasureAsWord

class ReportSummaryKtTest {

    companion object {
        @JvmStatic

        fun data() = listOf<Arguments>(
            Arguments.of("one", "two", "many", 0, "many"),
            Arguments.of("one", "two", "many", 1, "one"),
            Arguments.of("one", "two", "many", 2, "two"),
            Arguments.of("one", "two", "many", 3, "two"),
            Arguments.of("one", "two", "many", 4, "two"),
            Arguments.of("one", "two", "many", 5, "many"),
            Arguments.of("one", "two", "many", 95, "many")
        )
    }

    @ParameterizedTest(name = "SHOULD use form proper for given quantity")
    @MethodSource("data")
    fun test(one: String, two: String, many: String, quantity: Int, expected: String) {
        //when
        val actual = treasureAsWord(one, two, many, quantity)

        //then
        assertThat(actual).isEqualTo(expected)
    }
}