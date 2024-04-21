package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.AssetManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CustomInitializerForRoute(
    val storageHelper: StorageHelper,
    val assetManager: AssetManager
) {
    companion object {
        const val routeName = "custom"
    }
    private val markerFile = "copied_marker.txt"
    private val pathToDestination = storageHelper.pathToRoutesDir() + File.separator
    fun copyRouteToLocalStorage() {
        if (!isAlreadyCopied()) {
            copyRouteDefinition()
            copyPhotosAndSounds()
            markIsCopied()
        }
    }

    private fun copyRouteDefinition() {
        val inputStream = assetManager.open("$routeName.xml")
        copy(inputStream, storageHelper.getRouteFile(routeName))
    }

    private fun copyPhotosAndSounds() {
        val route = storageHelper.loadRoute(routeName)
        route.treasures.forEach { td ->
            td.photoFileName?.let { photoFileName ->
                val inputToPhoto = assetManager.open(photoFileName)
                copy(inputToPhoto, File(pathToDestination + photoFileName))
                td.photoFileName = pathToDestination + photoFileName
            }
            td.tipFileName?.let { soundFileName ->
                val inputToSound = assetManager.open(soundFileName)
                copy(inputToSound, File(pathToDestination + soundFileName))
                td.tipFileName = pathToDestination + soundFileName
            }
        }
        storageHelper.save(route)
    }

    private fun isAlreadyCopied(): Boolean {
        return File(pathToDestination + markerFile).exists()
    }

    private fun markIsCopied() {
        File(pathToDestination + markerFile).createNewFile()
    }

    private fun copy(inputStream: InputStream, outputFile: File) {
        val outputStream = FileOutputStream(outputFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
    }
}