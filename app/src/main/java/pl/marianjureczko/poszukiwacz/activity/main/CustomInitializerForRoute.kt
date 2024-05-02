package pl.marianjureczko.poszukiwacz.activity.main

import android.content.res.AssetManager
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File
import java.io.FileOutputStream

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
            copyMedia()
            markIsCopied()
        }
    }

    private fun copyRouteDefinition() {
        copy("$routeName.xml", storageHelper.getRouteFile(routeName))
    }

    private fun copyMedia() {
        val route = storageHelper.loadRoute(routeName)
        route.treasures.forEach { td ->
            td.photoFileName?.let { photoFileName ->
                td.photoFileName = copy(photoFileName, File(pathToDestination + photoFileName))
            }
            td.tipFileName?.let { soundFileName ->
                td.tipFileName = copy(soundFileName, File(pathToDestination + soundFileName))
            }
            td.movieFileName?.let { movieFileName ->
                td.movieFileName = copy(movieFileName, File(pathToDestination + movieFileName))
            }
            td.subtitlesFileName?.let { subtitlesFileName ->
                td.subtitlesFileName = copy(subtitlesFileName, File(pathToDestination + subtitlesFileName))
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

    private fun copy(assetFileName: String, outputFile: File): String {
        val inputStream = assetManager.open(assetFileName)
        val outputStream = FileOutputStream(outputFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return pathToDestination + assetFileName
    }
}