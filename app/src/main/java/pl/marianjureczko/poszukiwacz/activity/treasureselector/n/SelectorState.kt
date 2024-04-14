package pl.marianjureczko.poszukiwacz.activity.treasureselector.n

import android.net.Uri

data class SelectorState(
    var justFoundTreasureId: Int,
    var tempPhotoFileLocation: Uri,
    var cameraPermissionGranted: Boolean
)