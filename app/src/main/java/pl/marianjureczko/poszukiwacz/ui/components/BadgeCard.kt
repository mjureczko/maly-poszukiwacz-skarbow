package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.BadgeUiHelper
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge

@Composable
fun BadgeCard(
    badge: Badge,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
) {
    MyCard() {
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                modifier = Modifier
                    .height(80.dp)
                    .width(90.dp)// Match the height of the image
            ) {
                Image(
                    painterResource(BadgeUiHelper.badgeImage(badge)),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.matchParentSize()
                )
                Text(
                    style = MaterialTheme.typography.displayLarge.copy(
                        shadow = Shadow(
                            color = Color.White,
                            blurRadius = 8f,
                            offset = Offset(-8f, 2f)
                        )
                    ),
                    text = badge.level.toString(),
                    textAlign = TextAlign.Right,
                    modifier = Modifier.matchParentSize()
                )
            }
            val unitText = stringResource(BadgeUiHelper.badgeUnitText(badge))
            Text(
                style = textStyle,
                text = stringResource(R.string.badge_for) + " " + badge.achievementValue + " " + unitText,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp)
            )
            BadgeUiHelper.badgeUnitImage(badge)?.let { imageId ->
                Image(
                    painterResource(imageId),
                    contentDescription = "",
                    contentScale = ContentScale.Inside,
                    modifier = Modifier
                        .padding(start = 5.dp)
                        .height(40.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}