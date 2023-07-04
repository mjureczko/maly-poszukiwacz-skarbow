package pl.marianjureczko.poszukiwacz.activity.commemorative

import android.net.Uri
import androidx.lifecycle.ViewModel

class CommemorativeViewModel : ViewModel() {
    lateinit var commemorativePhotoAbsolutePath: String

    fun commemorativePhotoUri(): Uri = Uri.parse(commemorativePhotoAbsolutePath)
}