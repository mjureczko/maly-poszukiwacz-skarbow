package pl.marianjureczko.poszukiwacz.screen.badges

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.MyCard
import pl.marianjureczko.poszukiwacz.ui.components.Score
import pl.marianjureczko.poszukiwacz.usecase.badges.Badge

@Composable
fun BadgesScreenBody(
    modifier: Modifier,
    state: BadgesState,
) {
    Column(modifier = modifier.background(Color.White)) {
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(0.04.dh),
            horizontalArrangement = Arrangement.Center // Added to center content horizontally
        ) {
            Score(state.golds, R.drawable.gold, "gold image")
            Score(state.rubies, R.drawable.ruby, "ruby image")
            Score(state.diamonds, R.drawable.diamond, "diamond image")
            Score(state.knowledge, R.drawable.chest_small, "tourist treasure image")
        }
        Achievement(R.string.total_loot_collected, state.totalLoot())
        Achievement(R.string.discovered_treasures, state.treasures)
        Achievement(R.string.completed_routes, state.completedRoutes)
        Achievement(R.string.longest_completed_route, state.greatestNumberOfTreasuresOnRoute)
        if (state.showBadges()) {
            Badges(Modifier.weight(0.99f), state)
        }
        Spacer(modifier = Modifier.weight(0.01f))
        AdvertBanner()
    }
}

@Composable
private fun Achievement(@StringRes testId: Int, value: Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.headlineMedium,
        text = stringResource(testId) + " " + value,
        textAlign = TextAlign.Center
    )
}

@Composable
private fun Badges(
    modifier: Modifier,
    state: BadgesState,
) {
    Spacer(modifier = Modifier.height(20.dp))
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 2.dp, color = Color.Gray, shape = RoundedCornerShape(16.dp)),
    ) {
        Column(modifier = Modifier.background(Color.White)) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge.copy(textDecoration = TextDecoration.Underline),
                text = stringResource(R.string.badges_header),
                textAlign = TextAlign.Center
            )
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                modifier = Modifier.weight(0.99f)
            ) {
                items(state.badges) { badge ->
                    BadgeCard(badge, state)
                }
            }
        }
    }
}

@Composable
private fun BadgeCard(
    badge: Badge,
    state: BadgesState,
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
                    painterResource(state.badgeImage(badge)),
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
            val unitText = stringResource(state.badgeUnitText(badge))
            Text(
                style = MaterialTheme.typography.headlineMedium,
                text = stringResource(R.string.badge_for) + " " + badge.achievementValue + " " + unitText,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 15.dp)
            )
            state.badgeUnitImage(badge)?.let { imageId ->
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

