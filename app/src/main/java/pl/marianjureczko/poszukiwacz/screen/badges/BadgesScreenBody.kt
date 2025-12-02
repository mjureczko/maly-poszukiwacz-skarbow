package pl.marianjureczko.poszukiwacz.screen.badges

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.BadgeCard
import pl.marianjureczko.poszukiwacz.ui.components.Score

const val TOTAL_LOOT_ACHIEVEMENT = "Total loot collected achievement"
const val DISCOVERED_TREASURES_ACHIEVEMENT = "Discovered treasures achievement"
const val COMPLETED_ROUTES_ACHIEVEMENT = "Completed routes achievement"
const val LONGEST_COMPLETED_ROUTE_ACHIEVEMENT = "Longest completed route achievement"
@Composable
fun BadgesScreenBody(
    modifier: Modifier,
    state: BadgesState,
) {
    Column(modifier = modifier.background(Color.White)) {
        val scoresHeight = 0.04.dh
        Row(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .background(Color.Transparent)
                .height(scoresHeight),
            horizontalArrangement = Arrangement.Center // Added to center content horizontally
        ) {
            Score(state.golds, R.drawable.gold, "gold image", scoresHeight)
            Score(state.rubies, R.drawable.ruby, "ruby image", scoresHeight)
            Score(state.diamonds, R.drawable.diamond, "diamond image", scoresHeight)
            Score(state.knowledge, R.drawable.chest_small, "tourist treasure image", scoresHeight)
        }
        Achievement(R.string.total_loot_collected, state.totalLoot(), TOTAL_LOOT_ACHIEVEMENT)
        Achievement(R.string.discovered_treasures, state.treasures, DISCOVERED_TREASURES_ACHIEVEMENT)
        Achievement(R.string.completed_routes, state.completedRoutes, COMPLETED_ROUTES_ACHIEVEMENT)
        Achievement(
            R.string.longest_completed_route,
            state.greatestNumberOfTreasuresOnRoute,
            LONGEST_COMPLETED_ROUTE_ACHIEVEMENT
        )
        if (state.showBadges()) {
            Badges(Modifier.weight(0.99f), state)
        }
        Spacer(modifier = Modifier.weight(0.01f))
        AdvertBanner()
    }
}

@Composable
private fun Achievement(@StringRes testId: Int, value: Int, description: String = "") {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = description },
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
                    BadgeCard(badge)
                }
            }
        }
    }
}
