package pl.marianjureczko.poszukiwacz.activity.map.n

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mapbox.bindgen.Value
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.MapHelper
import pl.marianjureczko.poszukiwacz.ui.components.TopBar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MapScreen(
    navController: NavController,
    //TODO t: use StringResource instead
    resources: Resources,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide, onClickOnFacebook) },
        content = {
            MapScreenBody(resources)
        }
    )
}

@Composable
fun MapScreenBody(resources: Resources) {
    val viewModel: MapViewModel = hiltViewModel()
    val state = viewModel.state.value
    MapboxMap(state.route, resources)
}

@Composable
fun MapboxMap(route: Route, resources: Resources) {
    val mapView = MapView(App.getAppContext())

    MapHelper.renderTreasures(route, mapView, resources)
    mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style -> hideRoads(style) }

    AndroidView({ mapView }, Modifier.fillMaxSize())
    MapHelper.positionMapOnTreasures(route, mapView, 400.0)
}

private fun hideRoads(style: Style) {
    style.styleLayers.asSequence()
        .filter { l -> l.id.startsWith("road") }
        .forEach { l ->
            style.setStyleLayerProperty(l.id, "visibility", Value("none"))
        }
}