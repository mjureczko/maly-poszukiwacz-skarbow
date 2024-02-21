package pl.marianjureczko.poszukiwacz

import android.app.Application
import android.content.Context
import android.content.res.Resources
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class App : Application() {

    init { App.app = this }

    companion object {
        //TODO: debug (sending route over Bluetotth when more than one device is paired) with pool of size 1
        val executorService: ExecutorService = Executors.newFixedThreadPool(3)
        private lateinit var res: Resources
        private lateinit var app: App
        fun getAppContext(): Context = app.applicationContext
        fun getResources() = res
    }

    override fun onCreate() {
        super.onCreate()
        res = super.getResources();
    }
}