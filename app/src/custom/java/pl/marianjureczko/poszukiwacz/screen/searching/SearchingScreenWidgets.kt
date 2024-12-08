package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.Screen.dh

@Composable
fun Scores(score: Int, modifier: Modifier) {
    Row(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        //TODO if(isClassicMode)
        if (false) {
            Image(
                painterResource(R.drawable.gold),
                contentDescription = "gold image",
                contentScale = ContentScale.Inside,
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
            Image(
                painterResource(R.drawable.ruby),
                contentDescription = "ruby image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
            Image(
                painterResource(R.drawable.diamond),
                contentDescription = "diamond image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(start = 20.dp)
            )
            Text(
                color = Color.Gray,
                text = "0",
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize
            )
        } else {
            Image(
                painterResource(R.drawable.chest_small),
                contentDescription = "tourist treasure image",
                contentScale = ContentScale.Inside,
                modifier = Modifier.padding(end = 5.dp)
            )
            Text(
                color = Color.Gray,
                text = score.toString(),
                fontSize = pl.marianjureczko.poszukiwacz.ui.theme.Typography.h5.fontSize,
                modifier = Modifier.padding(end = 5.dp)
            )
        }
    }
}
