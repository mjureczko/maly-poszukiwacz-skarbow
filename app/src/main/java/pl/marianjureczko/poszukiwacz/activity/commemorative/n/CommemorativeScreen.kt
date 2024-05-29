package pl.marianjureczko.poszukiwacz.activity.commemorative.n

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.n.CommemorativeSharedState
import pl.marianjureczko.poszukiwacz.activity.searching.n.CommemorativeSharedViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.n.SharedViewModel
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.shareViewModelStoreOwner
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CommemorativeScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: () -> Unit,
    goToFacebook: () -> Unit,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(navController, stringResource(R.string.app_name), onClickOnGuide, goToFacebook) },
        content = {
            CommemorativeScreenBody(
                shareViewModelStoreOwner(navBackStackEntry, navController)
            )
        }
    )
}

@Composable
fun CommemorativeScreenBody(
    viewModelStoreOwner: NavBackStackEntry
) {
    val sharedViewModel: CommemorativeSharedViewModel = getViewModel(viewModelStoreOwner)
    val sharedState = sharedViewModel.state.value as CommemorativeSharedState
    val localViewModel: CommemorativeViewModel = hiltViewModel()
    val localState: CommemorativeState = localViewModel.state.value
    localViewModel.setPhotoPath(
        sharedState.treasuresProgress.commemorativePhotosByTreasuresDescriptionIds[localState.treasureDesId]!!
    )
    Column(Modifier.background(SecondaryBackground)) {
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        if (localState.photoPath != null) {
            val photo: Bitmap = BitmapFactory.decodeFile(localState.photoPath)
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
                DoPhotoButton(sharedViewModel, sharedState, localState)
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
    sharedViewModel: CommemorativeSharedViewModel,
    sharedState: CommemorativeSharedState,
    localState: CommemorativeState
) {
    val successMsg = stringResource(R.string.photo_replaced)
    val failureMsg = stringResource(R.string.photo_not_replaced)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                Toast.makeText(App.getAppContext(), successMsg, Toast.LENGTH_SHORT).show()
                sharedViewModel.handleDoCommemorativePhotoResult(
                    sharedState.route.treasures.find { it.id == localState.treasureDesId }!!
                )()
            } else {
                Toast.makeText(App.getAppContext(), failureMsg, Toast.LENGTH_SHORT).show()
            }
        }
    )
    Image(
        painter = painterResource(id = R.drawable.camera_do_photo),
        contentDescription = "Do a new commemorative photo",
        modifier = Modifier
            .height(50.dp)
            .offset(x = (-10).dp, y = (-10).dp)
            .clickable { cameraLauncher.launch(localState.tempPhotoFileLocation) },
        contentScale = ContentScale.Inside
    )
}

@Composable
private fun getViewModel(viewModelStoreOwner: NavBackStackEntry): CommemorativeSharedViewModel {
    val viewModelDoNotInline: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return viewModelDoNotInline
}