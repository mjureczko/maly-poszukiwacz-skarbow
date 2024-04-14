package pl.marianjureczko.poszukiwacz.activity.commemorative.n

import android.net.Uri

data class CommemorativeState(
    val treasureDesId: Int,
    val tempPhotoFileLocation: Uri,
    val photoPath: String?
)
