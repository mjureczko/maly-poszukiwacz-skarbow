package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.Coordinates

typealias OverrideQuestionProvider = (TreasureDescription) -> Boolean

data class TreasureEditorState(
    val route: Route,
    val currentLocation: Coordinates?,
    val overridePhotoQuestionProvider: OverrideQuestionProvider,
    val overrideSoundTipQuestionProvider: OverrideQuestionProvider,
    val showSoundRecordingDialog: Boolean = false,
    val fileForTipRecording: String? = null,
    val showOverridePhotoDialog: Boolean = false,
    val treasureWithPhotoTipToOverride: TreasureDescription? = null,
    val showOverrideSoundTipDialog: Boolean = false,
    val treasureWithSoundTipToOverride: TreasureDescription? = null,
) {
    fun locationBarData(): LocationBarData {
        val formatter = CoordinatesFormatter()
        val latitude = formatter.format(currentLocation?.latitude)
        val longitude = formatter.format(currentLocation?.longitude)
        val buttonEnabled = currentLocation != null
        return LocationBarData(latitude, longitude, buttonEnabled)
    }

    fun hasOnlyOneTreasure() = route.treasures.size == 1
}

data class LocationBarData(
    val latitude: String,
    val longitude: String,
    val buttonEnabled: Boolean
)