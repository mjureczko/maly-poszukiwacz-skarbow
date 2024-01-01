package pl.marianjureczko.poszukiwacz.shared

import android.content.res.AssetManager
import java.util.Properties

class Settings(assetManager: AssetManager) {
    private val properties = Properties()

    init {
        properties.load(assetManager.open("config.properties"))
    }

    fun isClassicMode(): Boolean = properties.getProperty("classicMode").toBoolean()
}