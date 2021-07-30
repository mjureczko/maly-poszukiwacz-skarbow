package pl.marianjureczko.poszukiwacz

import android.app.Application
import android.content.res.Resources
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class App : Application() {
    companion object {
        val executorService: ExecutorService = Executors.newSingleThreadExecutor()
        private lateinit var res: Resources
        fun getResources() =  res
    }

    override fun onCreate() {
        super.onCreate()
        res = super.getResources();
    }
}