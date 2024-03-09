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
class CustomMainViewModelTest {
    @Mock
    private lateinit var resources: Resources

    @BeforeEach
    fun setupMock() {
        BDDMockito.given(resources.getString(BDDMockito.anyInt()))
            .will{ someString() }
    }

    @Test
    fun `SHOULD increase message index WHEN calling next message`() {
        //given
        val viewModel = CustomMainViewModel(resources)
        assertThat(viewModel.state.value.messageIndex).isEqualTo(0)

        //when
        viewModel.nextLeadMessage()

        //then
        assertThat(viewModel.state.value.messageIndex).isEqualTo(1)
    }
}