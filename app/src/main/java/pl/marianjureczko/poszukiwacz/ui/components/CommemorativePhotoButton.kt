package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.screen.searching.DoCommemorative
import pl.marianjureczko.poszukiwacz.screen.searching.HasCommemorativePhoto

@Composable
fun CommemorativePhotoButton(
    isPermissionGranted: Boolean,
    hasCommemorativePhoto: HasCommemorativePhoto,
    goToCommemorative: (Int) -> Unit,
    doCommemorative: DoCommemorative,
    modifier: Modifier = Modifier,
    treasureDescriptionId: Int,
    updateImageRefresh: () -> Unit,
) {
    if (hasCommemorativePhoto.hasCommemorativePhoto(treasureDescriptionId)) {
        Image(
            painterResource(R.drawable.camera_show_photo),
            modifier = modifier
                .padding(2.dp)
                .height(35.dp)
                .clickable { goToCommemorative(treasureDescriptionId) },
            contentDescription = "Show commemorative photo",
            contentScale = ContentScale.Inside,
        )
    } else {
        val doPhoto = doCommemorative.getDoPhoto(isPermissionGranted, treasureDescriptionId) {
            updateImageRefresh()
        }
        Image(
            painterResource(R.drawable.camera_do_photo),
            modifier = modifier
                .padding(2.dp)
                .height(35.dp)
                .clickable { doPhoto() },
            contentDescription = "Do commemorative photo",
            contentScale = ContentScale.Inside,
        )
    }
}