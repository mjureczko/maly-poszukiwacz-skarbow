package pl.marianjureczko.poszukiwacz.screen.main

import android.content.res.AssetManager
import pl.marianjureczko.poszukiwacz.BuildConfig
import pl.marianjureczko.poszukiwacz.shared.port.StorageHelper
import java.io.File
import java.io.FileOutputStream

class CustomInitializerForRoute(
    private val storageHelper: StorageHelper,
    private val assetManager: AssetManager
) {
    companion object {
        const val routeName = "custom"

        //visibility for tests
        const val markerFile = "copied_marker.txt"
        private val currentVersion = BuildConfig.VERSION_CODE.toString()
    }


    private val pathToDestination = storageHelper.pathToRoutesDir() + File.separator
    fun copyRouteToLocalStorage() {
        if (!isAlreadyCopied()) {
            copyRouteDefinition()
            copyMedia()
            markIsCopied()
        }
    }

    fun isAlreadyCopied(): Boolean {
        return storageHelper.getFileContent(markerFile) == currentVersion
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

    private fun markIsCopied() {
        File(pathToDestination + markerFile).writeText(currentVersion)
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