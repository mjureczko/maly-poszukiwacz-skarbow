package pl.marianjureczko.poszukiwacz.shared.port

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.DoPhoto
import pl.marianjureczko.poszukiwacz.shared.errorTone

open class CameraPort(val context: Context) {

    /**
     * @return a callback that should be launched when the do photo button is clicked
     */
    @Composable
    open fun doPhoto(
        permissionGranted: Boolean,
        successMsg: Int,
        failureMsg: Int,
        getPhotoUri: () -> Uri,
        handleSuccess: () -> Unit = {}
        ): DoPhoto {

        val cameraLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicture(),
            onResult = { success ->
                if (success) {
                    handleSuccess()
                    Toast.makeText(context, successMsg, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(context, failureMsg, Toast.LENGTH_LONG).show()
                }
            }
        )

        val permissionErrorMsg = stringResource(R.string.photo_permission_not_granted)
        return {
            if(permissionGranted) {
                cameraLauncher.launch(getPhotoUri())
            } else {
                Toast.makeText(context, permissionErrorMsg, Toast.LENGTH_LONG).show()
                errorTone()
            }
        }
    }
}