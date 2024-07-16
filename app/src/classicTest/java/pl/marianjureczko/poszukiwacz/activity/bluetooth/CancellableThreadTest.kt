package pl.marianjureczko.poszukiwacz.activity.bluetooth

import com.ocadotechnology.gembus.test.some
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class CancellableThreadTest {
    @Test
    internal fun `SHOULD not print on console WHEN cancelled was called`() {
        //given
        var console = DummyMemoConsole()
        val cancellable = TestCancellableThread(console)
        cancellable.cancel()

        //when
        cancellable.testPrint(some<String>())

        //then
        assertThat(console.msgs).isEmpty()
    }

    @Test
    internal fun `SHOULD print on console WHEN cancelled was not called`() {
        //given
        var console = DummyMemoConsole()
        val cancellable = TestCancellableThread(console)
        val msg = some<String>()

        //when
        cancellable.testPrint(msg)

        //then
        assertThat(console.msgs).containsExactly(msg)
    }
}

class TestCancellableThread(console: DummyMemoConsole) : pl.marianjureczko.poszukiwacz.activity.bluetooth.CancellableThread(console) {
    fun testPrint(msg: String) = super.printInConsole(msg)
}

class DummyMemoConsole : pl.marianjureczko.poszukiwacz.activity.bluetooth.MemoConsole {
    val msgs = mutableListOf<String>()
    override fun print(msg: String) {
        msgs.add(msg)
    }
}