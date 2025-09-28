package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.screen.searching.LocationCalculator
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.RotatePhoto
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.MyCard
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import java.io.File
import java.io.FileOutputStream

@Composable
fun FacebookScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide
) {
    //TODO t:
//    val scaffoldState: ScaffoldState = rememberScaffoldState()
    Scaffold(
//        scaffoldState = scaffoldState,
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.title_activity_facebook),
                menuConfig = MenuConfig(onClickOnGuide)
            )
        },
        content = { paddingValues -> FacebookScreenBody(Modifier.padding(paddingValues)) }
    )
}

@Composable
fun FacebookScreenBody(modifier: Modifier) {
    val viewModel: FacebookViewModel = hiltViewModel()
    val state: FacebookState = viewModel.state.value

    Column(modifier.background(Color.White)) {
        SubHeader()
        Elements(Modifier.weight(0.99f), state, viewModel, viewModel.rotatePhoto())
        Spacer(modifier = Modifier.weight(0.01f))
        ShareOnFacebookButton(state, viewModel.locationCalculator)
        AdvertBanner()
    }
}

@Composable
private fun SubHeader() {
    Text(
        stringResource(R.string.facebook_screen_subheader),
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineMedium,
        textAlign = TextAlign.Center,
        color = Color.Black,
    )
}

@Composable
private fun Elements(
    modifier: Modifier,
    state: FacebookState,
    viewModel: FacebookViewModel,
    onRotatePhoto: RotatePhoto
) {
    LazyColumn(
        contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
        modifier = modifier
    ) {
        items(state.elements) {
            FacebookElement(it, viewModel, onRotatePhoto)
        }
    }
}

@Composable
private fun ShareOnFacebookButton(model: FacebookReportState, locationCalculator: LocationCalculator) {
    Box {
        val context = LocalContext.current
        val sharingErrorMsg = stringResource(R.string.facebook_share_error)
        val noFacebookErrorMsg = stringResource(id = R.string.facebook_share_impossible)
        LargeButton(R.string.share_button) {
            ReportGenerator().create(context, model, locationCalculator) { bitmap ->
                FacebookShareHelper.shareBitmapOnFacebook(context, bitmap, sharingErrorMsg, noFacebookErrorMsg)
            }
        }
        FacebookImage(Modifier.align(AbsoluteAlignment.CenterLeft))
        FacebookImage(Modifier.align(AbsoluteAlignment.CenterRight))
    }
}

@Composable
private fun FacebookImage(modifier: Modifier) {
    Image(
        painter = painterResource(id = R.drawable.facebook),
        contentDescription = "Share on Facebook icon",
        modifier = modifier
            .height(50.dp)
            .padding(10.dp),
        contentScale = ContentScale.Inside
    )
}

@Composable
fun FacebookElement(it: ElementDescription, viewModel: FacebookViewModel, onRotatePhoto: RotatePhoto) {
    MyCard {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            val imageId: Int = if (it.isSelected) {
                R.drawable.checkbox_checked
            } else {
                R.drawable.checkbox_empty
            }
            Image(
                painterResource(imageId),
                modifier = Modifier
                    .padding(2.dp)
                    .height(40.dp)
                    .clickable { viewModel.changeSelectionAt(it.index) },
                contentDescription = "Change treasure button",
                contentScale = ContentScale.Inside,
            )
            Text(
                text = it.description,
                style = MaterialTheme.typography.headlineMedium,
            )
            it.scaledPhoto?.let { photo ->
                val imageBitmap = photo.asImageBitmap()
                Image(
                    modifier = Modifier.padding(8.dp),
                    painter = BitmapPainter(imageBitmap),
                    contentDescription = "Small photo"
                )
                Image(
                    painter = painterResource(id = R.drawable.rotate_arc),
                    contentDescription = "Rotate photo",
                    modifier = Modifier
                        .padding(1.dp)
                        .height(60.dp)
                        .clickable { onRotatePhoto(it.index) },
                    contentScale = ContentScale.Inside
                )
            }
        }
    }
}

object FacebookShareHelper {

    fun shareBitmapOnFacebook(context: Context, bitmap: Bitmap, errorMsg: String, noFacebookErrorMsg: String) {
        val uri = bitmapToUri(context, bitmap)
        if (uri != null) {
            try {
                shareContent(context, uri)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(context, noFacebookErrorMsg, Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val file = File(context.cacheDir, "shared_bitmap.png")
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            return FileProvider.getUriForFile(context, context.packageName + ".fileprovider", file)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun shareContent(context: Context, contentUri: Uri) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "image/*"
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        shareIntent.`package` = "com.facebook.katana" // Specify Facebook package
        context.startActivity(shareIntent)
    }
}