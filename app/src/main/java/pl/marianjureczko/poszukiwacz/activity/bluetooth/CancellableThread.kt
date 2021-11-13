package pl.marianjureczko.poszukiwacz.activity.bluetooth

import java.util.concurrent.atomic.AtomicReference

abstract class CancellableThread(private val memoConsole: MemoConsole) : Thread() {
    private var cancelled: AtomicReference<Boolean> = AtomicReference(false)

    open fun cancel() {
        cancelled.set(true)
    }

    protected fun printInConsole(msg: String) {
        if (cancelled.get() == false) {
            memoConsole.print(msg)
        }
    }
}