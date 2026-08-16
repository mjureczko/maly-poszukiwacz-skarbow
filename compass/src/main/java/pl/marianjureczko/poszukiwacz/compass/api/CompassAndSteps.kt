package pl.marianjureczko.poszukiwacz.compass.api

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.marianjureczko.poszukiwacz.compass.Compass
import pl.marianjureczko.poszukiwacz.compass.Steps
import pl.marianjureczko.poszukiwacz.compass.data.HunterPathService
import pl.marianjureczko.poszukiwacz.compass.model.Route
import pl.marianjureczko.poszukiwacz.compass.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.compass.viewmodel.CompassViewModel

@Composable
fun CompassAndSteps(
    selectedTreasure: TreasureDescription?,
    route: Route,
    hunterPathService: HunterPathService,
    height: Dp = 0.49.dp,
    textStyle: TextStyle = TextStyle(),
    modifier: Modifier = Modifier,
) {
    val viewModel: CompassViewModel = hiltViewModel()

    LaunchedEffect(selectedTreasure, route) {
        viewModel.setSelectedTreasure(selectedTreasure)
        viewModel.setRoute(route)
    }

    val compassHeight = height * (0.35f / 0.49f)
    val stepsHeight = height * (0.14f / 0.49f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Compass(
            arcRotation = viewModel.state.value.needleRotation,
            gpsAccuracy = viewModel.state.value.gpsAccuracy,
            modifier = Modifier.fillMaxWidth(),
            height = compassHeight
        )
        Steps(
            stepsToTreasure = viewModel.state.value.stepsToTreasure,
            textStyle = textStyle,
            modifier = Modifier.padding(top = 8.dp),
            height = stepsHeight
        )
    }
}
