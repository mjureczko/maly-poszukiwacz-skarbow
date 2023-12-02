package pl.marianjureczko.poszukiwacz.activity.commemorative

import android.net.Uri
import androidx.lifecycle.ViewModel
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class CommemorativeViewModel : ViewModel() {
    lateinit var commemorativePhotoAbsolutePath: String
        private set
    lateinit var progress: TreasuresProgress
        private set

    fun initialize(progress: TreasuresProgress, commemorativePhotoAbsolutePath: String) {
        this.progress = progress
        this.commemorativePhotoAbsolutePath = commemorativePhotoAbsolutePath
    }

    fun commemorativePhotoUri(): Uri = Uri.parse(commemorativePhotoAbsolutePath)
}