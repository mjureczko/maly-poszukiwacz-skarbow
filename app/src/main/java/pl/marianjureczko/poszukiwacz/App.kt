package pl.marianjureczko.poszukiwacz

import android.app.Application
import android.content.res.Resources

class App : Application() {
    companion object {
        private lateinit var res: Resources
        fun getResources() =  res
    }

    override fun onCreate() {
        super.onCreate()
        res = super.getResources();
    }
}