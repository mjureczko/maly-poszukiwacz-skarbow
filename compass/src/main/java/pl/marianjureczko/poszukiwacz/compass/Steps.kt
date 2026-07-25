package pl.marianjureczko.poszukiwacz.compass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Steps(stepsToTreasure: Int?, textStyle: TextStyle, modifier: Modifier = Modifier, height: Dp) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(height),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Top,
    ) {
        if (stepsToTreasure != null) {
            Text(
                modifier = Modifier
                    .padding(start = 40.dp)
                    .semantics { contentDescription = "Steps to treasure" },
                style = textStyle,
                color = Color.Gray,
                text = stepsToTreasure.toString()
            )
        } else {
            CircularProgressIndicator(Modifier.semantics { this.contentDescription = "Waiting for GPS" })
        }
        Image(
            painterResource(R.drawable.steps),
            modifier = Modifier.padding(start = 43.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}