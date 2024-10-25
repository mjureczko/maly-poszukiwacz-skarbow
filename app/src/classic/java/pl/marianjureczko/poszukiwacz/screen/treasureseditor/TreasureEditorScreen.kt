package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CameraAlt
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Mic
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.MapHelper
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.EmbeddedButton
import pl.marianjureczko.poszukiwacz.ui.components.TopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TreasureEditorScreen(
    navController: NavController,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit
) {
    val viewModel: TreasureEditorViewModel = hiltViewModel()
    val state = viewModel.state.value
    Scaffold(
        topBar = {
            TopBar(
                navController,
                "${stringResource(id = R.string.route)} ${state.route.name}",
                onClickOnGuide,
                onClickOnFacebook
            )
        },
        content = { _ -> TreasureEditorScreenBody(state) }
    )
}

@Composable
fun TreasureEditorScreenBody(state: TreasureEditorState) {
    Column {

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
            modifier = Modifier.weight(0.99f)
        ) {
            items(state.route.treasures) { treasure ->
                TreasureItem(treasure)
            }
        }
        Spacer(modifier = Modifier.weight(0.02f))
        LiveMap(state.route)
        LocationBar(state.locationBarData())
        Spacer(modifier = Modifier.weight(0.001f))
        val isInPreview = LocalInspectionMode.current
        if (!isInPreview) {
            AdvertBanner()
        }
    }
}

@Composable
private fun TreasureItem(treasure: TreasureDescription) {
    Card(
        elevation = 4.dp,
        modifier = Modifier.padding(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = treasure.prettyName(),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            )
            EmbeddedButton(Icons.TwoTone.CameraAlt) {}
            EmbeddedButton(Icons.TwoTone.Mic) {}
            EmbeddedButton(Icons.TwoTone.Delete) {}
        }
    }
}

@Composable
fun LiveMap(route: Route) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(3.dp, Color.LightGray)
    ) {
        val isInPreview = LocalInspectionMode.current
        if (!isInPreview) {
            val context = LocalContext.current
            val mapView = MapView(context)

//        MapHelper.renderTreasures(context, route, mapView)
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

            AndroidView({ mapView }, Modifier.fillMaxSize())
            MapHelper.positionMapOnTreasures(route, mapView, 400.0)
        }
    }
}

@Composable
fun LocationBar(location: LocationBarData) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = stringResource(id = R.string.latitude) + ":",
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(id = R.string.longitude) + ":",
                modifier = Modifier.padding(4.dp)
            )
        }
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = location.latitude,
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = location.longitude,
                modifier = Modifier.padding(4.dp)
            )
        }
        Image(
            painterResource(R.drawable.chest_add_small),
            modifier = Modifier
                .padding(start = 70.dp, top = 8.dp)
                .height(50.dp)
                .width(70.dp)
                .clickable(enabled = location.buttonEnabled) { },
            contentDescription = "Add treasure button",
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview(showBackground = true, apiLevel = 31, backgroundColor = 0xffffff)
@Composable
fun TreasureEditorScreenBodyPreview() {
    TreasureEditorScreenBody(
        TreasureEditorState(
            Route("name", mutableListOf(TreasureDescription())), null
        )
    )
}