package pl.marianjureczko.poszukiwacz.compass.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.compass.Compass
import pl.marianjureczko.poszukiwacz.compass.GpsAccuracy
import pl.marianjureczko.poszukiwacz.compass.Steps

@Composable
fun CompassAndSteps(
    needleRotation: Float,
    gpsAccuracy: GpsAccuracy,
    height: Dp,
    textStyle: TextStyle,
    stepsToTreasure: Int?,
    modifier: Modifier = Modifier
) {
    val compassHeight = height * (0.35f / 0.49f)
    val stepsHeight = height * (0.14f / 0.49f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Compass(needleRotation, gpsAccuracy, Modifier.fillMaxWidth(), compassHeight)
        Steps(stepsToTreasure, textStyle, Modifier.padding(top = 8.dp), stepsHeight)
    }
}
