package pl.marianjureczko.poszukiwacz.activity.photo.n

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TipPhotoScreen(
    navController: NavController,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, stringResource(R.string.photo_tip), onClickOnGuide, onClickOnFacebook) },
        content = {
            TipPhotoScreenBody()
        }
    )
}

@Composable
fun TipPhotoScreenBody() {
    val viewModel: TipPhotoViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column(Modifier.background(SecondaryBackground)) {
        Spacer(
            modifier = Modifier
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
