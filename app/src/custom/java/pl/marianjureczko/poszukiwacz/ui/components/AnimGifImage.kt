package pl.marianjureczko.poszukiwacz.ui.components

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import pl.marianjureczko.poszukiwacz.screen.main.GUIDE_IMAGE

@Composable
fun AnimGifImage(imageId: Int) {
    val imageLoader = ImageLoader.Builder(LocalContext.current)
        .components {
            if (Build.VERSION.SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
    AsyncImage(
        model = imageId,
        imageLoader = imageLoader,
        contentDescription = GUIDE_IMAGE
    )
}
