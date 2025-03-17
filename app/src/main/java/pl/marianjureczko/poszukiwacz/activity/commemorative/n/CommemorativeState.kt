package pl.marianjureczko.poszukiwacz.activity.commemorative.n

import android.net.Uri

data class CommemorativeState(
    val treasureDesId: Int,
    val tempPhotoFileLocation: Uri,
    // For triggering recomposition
    val photoVersion: Int = 0,
    val photoPath: String?,
)
