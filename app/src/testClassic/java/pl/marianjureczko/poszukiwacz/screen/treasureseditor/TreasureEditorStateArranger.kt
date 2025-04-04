package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import com.ocadotechnology.gembus.test.CustomArranger
import com.ocadotechnology.gembus.test.some
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.Coordinates

class TreasureEditorStateArranger : CustomArranger<TreasureEditorState>() {
    override fun instance(): TreasureEditorState {
        return TreasureEditorState(
            route = some<Route>(),
            currentLocation = some<Coordinates>(),
            overridePhotoQuestionProvider = {_ -> false},
            overrideSoundTipQuestionProvider = {_ -> false},
            showSoundRecordingDialog = some<Boolean>(),
            fileForTipRecording = some<String>(),
            showOverridePhotoDialog = some<Boolean>(),
            showOverrideSoundTipDialog = some<Boolean>()
        )
    }
}