package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.Resources
import com.ocadotechnology.gembus.test.someString
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class CustomMainStateTest {

    @Mock
    private lateinit var resources: Resources

    @BeforeEach
    fun setupMock() {
        BDDMockito.given(resources.getString(BDDMockito.anyInt()))
            .will{ someString() }
    }

    @Test
    fun `SHOULD say not last message WHEN it is the first message`() {
        //given
        val initialState = CustomMainState(resources)

        //then
        assertThat(initialState.messageIndex).isEqualTo(0)
        assertThat(initialState.isLastMessage()).isFalse()
    }

    @Test
    fun `SHOULD say last message WHEN it is the last message`() {
        //given
        var state = CustomMainState(resources)
        state = state.copy(messageIndex = state.messages.size -1)

        //then
        assertThat(state.isLastMessage()).isTrue()
    }
}