package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates

data class TreasureEditorState(
    val route: Route,
    val currentLocation: Coordinates?
) {
    fun locationBarData(): LocationBarData {
        val formatter = CoordinatesFormatter()
        val latitude = formatter.format(currentLocation?.latitude)
        val longitude = formatter.format(currentLocation?.longitude)
        val buttonEnabled = currentLocation != null
        return LocationBarData(latitude, longitude, buttonEnabled)
    }
}

data class LocationBarData(
    val latitude: String,
    val longitude: String,
    val buttonEnabled: Boolean
)