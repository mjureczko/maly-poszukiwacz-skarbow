package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.AssetManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class CustomInitializerForRoute(
    val storageHelper: StorageHelper,
    val assetManager: AssetManager) {
    companion object {
        //TODO: classic needs different solution for delivering route name as it cannot be the custom default
        const val routeName = "???"
    }
}