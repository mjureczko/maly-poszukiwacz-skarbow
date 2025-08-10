package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.AssetManager
import pl.marianjureczko.poszukiwacz.shared.port.storage.StoragePort

class CustomInitializerForRoute(
    val storagePort: StoragePort,
    val assetManager: AssetManager) {
}