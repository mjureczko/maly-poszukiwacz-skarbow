package pl.marianjureczko.poszukiwacz.ui.components

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.n.HasCommemorativePhoto
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.DoPhotoResultHandler
import pl.marianjureczko.poszukiwacz.shared.GoToCommemorative

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CommemorativePhotoButton(
    cameraPermission: PermissionState,
    hasCommemorativePhoto: HasCommemorativePhoto,
    tmpPhoto: Uri,
    treasure: TreasureDescription,
    goToCommemorative: GoToCommemorative,
    handleDoPhotoResult: DoPhotoResultHandler,
    modifier: Modifier = Modifier
) {
    if (cameraPermission.status.isGranted) {
        if (hasCommemorativePhoto.hasCommemorativePhoto(treasure.id)) {
            Image(
                painterResource(R.drawable.camera_show_photo),
                modifier = modifier
                    .padding(2.dp)
                    .height(35.dp)
                    .clickable { goToCommemorative(treasure.id) },
                contentDescription = "Show commemorative photo",
                contentScale = ContentScale.Inside,
            )
        } else {
            val successMsg = stringResource(R.string.photo_saved)
            val failureMsg = stringResource(R.string.photo_not_replaced)
            val cameraLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.TakePicture(),
                onResult = { success ->
                    if (success) {
                        handleDoPhotoResult()
                        Toast.makeText(App.getAppContext(), successMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(App.getAppContext(), failureMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            )
            Image(
                painterResource(R.drawable.camera_do_photo),
                modifier = modifier
                    .padding(2.dp)
                    .height(35.dp)
                    .clickable { cameraLauncher.launch(tmpPhoto) },
                contentDescription = "Do commemorative photo",
                contentScale = ContentScale.Inside,
            )
        }
    }
}