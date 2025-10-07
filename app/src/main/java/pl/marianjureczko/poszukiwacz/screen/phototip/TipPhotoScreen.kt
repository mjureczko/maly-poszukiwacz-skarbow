package pl.marianjureczko.poszukiwacz.screen.phototip

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.screen.searching.RestarterSharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.GoToBadgesScreen
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.components.ViewModelProgressRestarter
import pl.marianjureczko.poszukiwacz.ui.getSharedViewModel

@Composable
fun TipPhotoScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook,
    onClickBadges: GoToBadgesScreen,
) {
    val viewModel: TipPhotoViewModel = hiltViewModel()
    val state: TipPhotoState = viewModel.state.value
    val shared: RestarterSharedViewModel = getSharedViewModel(navBackStackEntry, navController)
    val restarter = ViewModelProgressRestarter { shared.restartProgress() }
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.photo_tip),
                menuConfig = tipPhotoMenuConfig(onClickOnGuide, onClickOnFacebook, state, restarter, onClickBadges)
            )
        },
        content = { paddingValues ->
            TipPhotoScreenBody(Modifier.padding(paddingValues), state)
        }
    )
}

@Composable
fun TipPhotoScreenBody(
    modifier: Modifier,
    state: TipPhotoState
) {
    Column(modifier = modifier) {
        Spacer(
            modifier = modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        val photo: Bitmap = BitmapFactory.decodeFile(state.filePath)
        val aspectRatio = photo.width.toFloat() / photo.height.toFloat()
        Image(
            painter = BitmapPainter(photo.asImageBitmap()),
            contentDescription = "Image with a tip",
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
                .aspectRatio(aspectRatio),
            contentScale = ContentScale.FillBounds,
        )
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        AdvertBanner()
    }
}
