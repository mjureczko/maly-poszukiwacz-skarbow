package pl.marianjureczko.poszukiwacz.screen.treasureselector

import android.net.Uri

data class SelectorState(
    var justFoundTreasureId: Int,
    var tempPhotoFileLocation: Uri
)