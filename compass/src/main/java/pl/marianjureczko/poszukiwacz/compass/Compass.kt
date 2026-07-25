package pl.marianjureczko.poszukiwacz.compass

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Compass(
    arcRotation: Float,
    gpsAccuracy: GpsAccuracy,
    modifier: Modifier = Modifier,
    height: Dp
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha = rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        )
    ).value

    Box(
        modifier = modifier
            .padding(start = 15.dp, end = 15.dp, top = 1.dp, bottom = 1.dp)
            .fillMaxWidth()
            .height(height)
            .semantics { contentDescription = "Compass" },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painterResource(R.drawable.compass),
            contentDescription = "compass face",
            contentScale = ContentScale.Inside,
        )
        Image(
            painter = painterResource(R.drawable.arrow),
            contentDescription = "compass needle",
            contentScale = ContentScale.Inside,
            modifier = Modifier.rotate(arcRotation)
        )
        if (gpsAccuracy != GpsAccuracy.Fine) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomStart
            ) {
                val textResId = when (gpsAccuracy) {
                    GpsAccuracy.Medium -> R.string.medium_gps_signal
                    GpsAccuracy.Low -> R.string.low_gps_signal
                    else -> R.string.no_gps_signal
                }
                Text(
                    text = stringResource(textResId),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Red.copy(alpha = alpha),
                    modifier = Modifier.padding(end = 1.dp, bottom = 1.dp)
                )
            }
        }
    }
}