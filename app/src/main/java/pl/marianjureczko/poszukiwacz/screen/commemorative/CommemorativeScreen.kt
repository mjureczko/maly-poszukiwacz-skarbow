package pl.marianjureczko.poszukiwacz.screen.commemorative

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingCommemorativePhoto
import pl.marianjureczko.poszukiwacz.screen.searching.CommemorativeSharedState
import pl.marianjureczko.poszukiwacz.screen.searching.CommemorativeSharedViewModel
import pl.marianjureczko.poszukiwacz.screen.searching.DoCommemorative
import pl.marianjureczko.poszukiwacz.screen.searching.SharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermission
import pl.marianjureczko.poszukiwacz.ui.shareViewModelStoreOwner

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CommemorativeScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    goToFacebook: GoToFacebook,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val sharedViewModel: CommemorativeSharedViewModel =
        getViewModel(shareViewModelStoreOwner(navBackStackEntry, navController))
    val sharedState = sharedViewModel.state.value as CommemorativeSharedState
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.app_name),
                onClickOnGuide = onClickOnGuide,
                onClickOnFacebook = { goToFacebook(sharedState.route.name) })
        },
        content = { CommemorativeScreenBody(sharedViewModel, sharedState) }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CommemorativeScreenBody(
    sharedViewModel: CommemorativeSharedViewModel,
    sharedState: CommemorativeSharedState,
) {
    val cameraPermission: PermissionState = handlePermission(RequirementsForDoingCommemorativePhoto)
    val localViewModel: CommemorativeViewModel = hiltViewModel()
    val localState: CommemorativeState = localViewModel.state.value
    localViewModel.setPhotoPath(
        sharedState.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds[localState.treasureDesId]!!
    )
    Column {
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        if (localState.photoPath != null) {
            val photo: Bitmap = remember(localState.photoVersion) {
                BitmapFactory.decodeFile(PhotoHelper.decodePhotoPath(localState.photoPath))
            }
            val aspectRatio = photo.width.toFloat() / photo.height.toFloat()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.9f),
                contentAlignment = Alignment.BottomEnd
            ) {
                Image(
                    painter = BitmapPainter(photo.asImageBitmap()),
                    contentDescription = "Photo from the hunt",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                        .align(Alignment.Center)
                        .aspectRatio(aspectRatio),
                    contentScale = ContentScale.FillBounds,
                )
                DoPhotoButton(localState, sharedViewModel, cameraPermission.status.isGranted) {
                    localViewModel.updatePhotoVersionForRefresh()
                }
                Image(
                    painter = painterResource(id = R.drawable.rotate_arc),
                    contentDescription = "Rotate commemorative photo",
                    modifier = Modifier
                        .height(50.dp)
                        .offset(x = (-10).dp, y = (-70).dp)
                        .clickable { localViewModel.rotatePhoto() },
                    contentScale = ContentScale.Inside
                )
            }
        }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        AdvertBanner()
    }
}

@Composable
private fun DoPhotoButton(
    localState: CommemorativeState,
    doCommemorative: DoCommemorative,
    cameraPermissionGranted: Boolean,
    updateImageRefresh: () -> Unit,
) {
    val doPhoto = doCommemorative.getDoPhoto(cameraPermissionGranted, localState.treasureDesId, updateImageRefresh)
    Image(
        painter = painterResource(id = R.drawable.camera_do_photo),
        contentDescription = "Do a new commemorative photo",
        modifier = Modifier
            .height(50.dp)
            .offset(x = (-10).dp, y = (-10).dp)
            .clickable { doPhoto() },
        contentScale = ContentScale.Inside
    )
}

//TODO: learn more about sharing view model
@Composable
private fun getViewModel(viewModelStoreOwner: NavBackStackEntry): CommemorativeSharedViewModel {
    val viewModelDoNotInline: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return viewModelDoNotInline
}