package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.Resources
import com.ocadotechnology.gembus.test.someString
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class MainViewModelTest {

    private val dispatcher: TestDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var resources: Resources

    @Mock
    private lateinit var customInitializerForRoute: CustomInitializerForRoute

    @BeforeEach
    fun setupMock() {
        BDDMockito.given(resources.getString(BDDMockito.anyInt()))
            .will{ someString() }
    }

    @Test
    fun `SHOULD increase message index WHEN calling next message`() {
        //given
        val viewModel = MainViewModel(resources, dispatcher, customInitializerForRoute)
        assertThat(viewModel.state.value.messageIndex).isEqualTo(0)

        //when
        viewModel.nextLeadMessage()

        //then
        assertThat(viewModel.state.value.messageIndex).isEqualTo(1)
    }
}