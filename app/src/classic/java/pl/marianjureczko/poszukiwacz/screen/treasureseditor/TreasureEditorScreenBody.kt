package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.AddTreasure
import pl.marianjureczko.poszukiwacz.shared.DoPhoto
import pl.marianjureczko.poszukiwacz.shared.HideOverridePhotoDialog
import pl.marianjureczko.poszukiwacz.shared.HideOverrideSoundTipDialog
import pl.marianjureczko.poszukiwacz.shared.HideSoundRecordingDialog
import pl.marianjureczko.poszukiwacz.shared.MapHelper
import pl.marianjureczko.poszukiwacz.shared.RemoveTreasure
import pl.marianjureczko.poszukiwacz.shared.ShowOverridePhotoDialog
import pl.marianjureczko.poszukiwacz.shared.ShowOverrideSoundTipDialog
import pl.marianjureczko.poszukiwacz.shared.ShowSoundRecordingDialog
import pl.marianjureczko.poszukiwacz.shared.errorTone
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.EmbeddedButton
import pl.marianjureczko.poszukiwacz.ui.components.YesNoDialog

const val TREASURE_ITEM_ROW = "Treasure row"
const val TREASURE_ITEM_TEXT = "Treasure"
const val ADD_TREASURE_BUTTON = "Add treasure button"
const val RECORD_SOUND_TIP_BUTTON = "Record sound tip button"
const val DO_PHOTO_TIP_BUTTON = "Do photo tip button"

@Composable
fun TreasureEditorScreenBody(
    state: TreasureEditorState,
    cameraPermissionGranted: Boolean,
    recordingPermissionGranted: Boolean,
    hideOverrideSoundTipDialog: HideOverrideSoundTipDialog,
    hideOverridePhotoDialog: HideOverridePhotoDialog,
    showOverridePhotoDialog: ShowOverridePhotoDialog,
    showOverrideSoundTipDialog: ShowOverrideSoundTipDialog,
    showSoundRecordingDialog: ShowSoundRecordingDialog,
    hideSoundRecordingDialog: HideSoundRecordingDialog,
    addTreasure: AddTreasure,
    removeTreasure: RemoveTreasure,
    getDoTipPhoto: GetDoTipPhoto
) {
    Box(contentAlignment = Alignment.Center) {
        Column {
            LazyColumn(
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp),
                modifier = Modifier.weight(0.99f)
            ) {
                items(state.route.treasures) { treasure ->
                    TreasureItem(
                        treasure = treasure,
                        cameraPermissionGranted = cameraPermissionGranted,
                        recordingPermissionGranted = recordingPermissionGranted,
                        state = state,
                        showOverridePhotoDialog = showOverridePhotoDialog,
                        showOverrideSoundTipDialog = showOverrideSoundTipDialog,
                        showSoundRecordingDialog = showSoundRecordingDialog,
                        removeTreasure = removeTreasure,
                        getDoTipPhoto = getDoTipPhoto
                    )
                }
            }
            Spacer(modifier = Modifier.weight(0.02f))
            LiveMap(state.route)
            LocationBar(state.locationBarData(), addTreasure)
            Spacer(modifier = Modifier.weight(0.001f))
            val isInPreview = LocalInspectionMode.current
            if (!isInPreview) {
                AdvertBanner()
            }
        }
        if (state.showSoundRecordingDialog) {
            RecordingDialog(state.fileForTipRecording!!) { _ -> hideSoundRecordingDialog() }
        }
        if (state.showOverrideSoundTipDialog) {
            OverrideSoundTipDialog(state, hideOverrideSoundTipDialog, showSoundRecordingDialog)
        }
        if (state.showOverridePhotoDialog) {
            OverridePhotoTipDialog(
                cameraPermissionGranted = cameraPermissionGranted,
                state = state,
                hideOverridePhotoDialog = hideOverridePhotoDialog,
                getDoPhoto = getDoTipPhoto,
            )
        }
    }
}

@Composable
fun TreasureItem(
    treasure: TreasureDescription,
    cameraPermissionGranted: Boolean,
    recordingPermissionGranted: Boolean,
    state: TreasureEditorState,
    showOverridePhotoDialog: ShowOverridePhotoDialog,
    showOverrideSoundTipDialog: ShowOverrideSoundTipDialog,
    showSoundRecordingDialog: ShowSoundRecordingDialog,
    removeTreasure: RemoveTreasure,
    getDoTipPhoto: GetDoTipPhoto
) {
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
                .semantics { contentDescription = TREASURE_ITEM_ROW }
        ) {
            Text(
                text = treasure.prettyName(),
                style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .semantics { contentDescription = "$TREASURE_ITEM_TEXT ${treasure.id}" }
            )
            DoPhotoButton(
                treasure = treasure,
                cameraPermissionGranted = cameraPermissionGranted,
                state = state,
                description = "$DO_PHOTO_TIP_BUTTON ${treasure.id}",
                showOverridePhotoDialog = showOverridePhotoDialog,
                getDoPhoto = getDoTipPhoto
            )
            RecordSoundTipButton(
                treasure,
                recordingPermissionGranted,
                state,
                "$RECORD_SOUND_TIP_BUTTON ${treasure.id}",
                showOverrideSoundTipDialog,
                showSoundRecordingDialog
            )
            RemoveTreasureButton(state, removeTreasure, treasure)
        }
    }
}

@Composable
private fun RemoveTreasureButton(
    state: TreasureEditorState,
    removeTreasure: RemoveTreasure,
    treasure: TreasureDescription
) {
    val ctx = LocalContext.current
    EmbeddedButton(imageVector = Icons.TwoTone.Delete) {
        if (state.hasOnlyOneTreasure()) {
            errorTone()
            Toast.makeText(ctx, ctx.getString(R.string.cannot_remove_last_treasure), Toast.LENGTH_SHORT).show()
        } else {
            removeTreasure(treasure.id)
        }
    }
}

@Composable
private fun OverrideSoundTipDialog(
    state: TreasureEditorState,
    hideOverrideSoundTipDialog: HideOverrideSoundTipDialog,
    showSoundRecordingDialog: ShowSoundRecordingDialog,
) {
    YesNoDialog(
        state.showOverrideSoundTipDialog,
        hideOverrideSoundTipDialog,
        R.string.overwritting_tip
    ) {
        showSoundRecordingDialog(state.treasureWithSoundTipToOverride!!)
    }
}

@Composable
private fun RecordSoundTipButton(
    treasure: TreasureDescription,
    recordingPermissionGranted: Boolean,
    state: TreasureEditorState,
    description: String,
    showOverrideSoundTipDialog: ShowOverrideSoundTipDialog,
    showSoundRecordingDialog: ShowSoundRecordingDialog
) {
    val permissionErrorMsg = stringResource(R.string.recording_permission_not_granted)
    val context = LocalContext.current
    EmbeddedButton(
        imageVector = Icons.TwoTone.Mic,
        description = description
    ) {
        if (recordingPermissionGranted) {
            if (state.overrideSoundTipQuestionProvider(treasure)) {
                showOverrideSoundTipDialog(treasure)
            } else {
                showSoundRecordingDialog(treasure)
            }
        } else {
            Toast.makeText(context, permissionErrorMsg, Toast.LENGTH_LONG).show()
            errorTone()
        }
    }
}

@Composable
private fun OverridePhotoTipDialog(
    cameraPermissionGranted: Boolean,
    state: TreasureEditorState,
    hideOverridePhotoDialog: HideOverridePhotoDialog,
    getDoPhoto: GetDoTipPhoto,
) {
    val launchDoPhoto = getDoPhoto.getDoPhoto(cameraPermissionGranted, state.treasureWithPhotoTipToOverride!!)
    YesNoDialog(
        state.showOverridePhotoDialog,
        hideOverridePhotoDialog,
        R.string.overwritting_photo
    ) {
        launchDoPhoto()
    }
}

@Composable
private fun DoPhotoButton(
    treasure: TreasureDescription,
    cameraPermissionGranted: Boolean,
    state: TreasureEditorState,
    description: String,
    showOverridePhotoDialog: ShowOverridePhotoDialog,
    getDoPhoto: GetDoTipPhoto
) {
    val launchDoPhoto = getDoPhoto.getDoPhoto(cameraPermissionGranted, treasure)
    EmbeddedButton(
        imageVector = Icons.TwoTone.CameraAlt,
        description = description
    ) {
        if (state.overridePhotoQuestionProvider(treasure)) {
            showOverridePhotoDialog(treasure)
        } else {
            launchDoPhoto()
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

            MapHelper.renderTreasures(context, route, mapView)
            mapView.getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS)

            AndroidView({ mapView }, Modifier.fillMaxSize())
            MapHelper.positionMapOnTreasures(route, mapView, 400.0)
        }
    }
}


@Composable
fun LocationBar(location: LocationBarData, addTreasure: AddTreasure) {
    Row(
        modifier = Modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.padding(4.dp)) {
            Text(
                text = stringResource(id = R.string.latitude),
                modifier = Modifier.padding(4.dp)
            )
            Text(
                text = stringResource(id = R.string.longitude),
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
        val ctx = LocalContext.current
        Image(
            painterResource(R.drawable.chest_add_small),
            modifier = Modifier
                .padding(start = 70.dp, top = 8.dp)
                .height(50.dp)
                .width(70.dp)
                .clickable(
                    onClick = {
                        if (location.buttonEnabled) addTreasure()
                        else {
                            Toast
                                .makeText(ctx, ctx.getString(R.string.no_gps), Toast.LENGTH_SHORT)
                                .show()
                            errorTone()
                        }
                    },
                ),
            contentDescription = ADD_TREASURE_BUTTON,
            contentScale = ContentScale.FillBounds,
        )
    }
}

@Preview(showBackground = true, apiLevel = 31, backgroundColor = 0xffffff)
@Composable
fun TreasureEditorScreenBodyPreview() {
    TreasureEditorScreenBody(
        TreasureEditorState(
            Route("name", mutableListOf(TreasureDescription())),
            null, { _ -> false },
            { _ -> false }
        ),
        false, false, {}, {}, {}, { _ -> }, {}, {}, {}, { _ -> }, object : GetDoTipPhoto {
            @Composable
            override fun getDoPhoto(cameraPermissionGranted: Boolean, treasure: TreasureDescription): DoPhoto = {}
        }
    )
}
