package pl.marianjureczko.poszukiwacz

import android.net.Uri
import androidx.compose.runtime.Composable
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.shared.DoPhoto
import pl.marianjureczko.poszukiwacz.shared.port.CameraPort

class TestCameraPort : CameraPort(mock()) {
    var counter: Int = 0

    @Composable
    override fun doPhoto(
        permissionGranted: Boolean,
        successMsg: Int,
        failureMsg: Int,
        getPhotoUri: () -> Uri,
        handleSuccess: () -> Unit,
    ): DoPhoto {
        return {
            counter++
        }
    }
}