package pl.marianjureczko.poszukiwacz

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    companion object {
//        //TODO: debug (sending route over Bluetotth when more than one device is paired) with pool of size 1
//        val executorService: ExecutorService = Executors.newFixedThreadPool(3)
    }
}