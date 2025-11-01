package pl.marianjureczko.poszukiwacz.shared

import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.result.ResultType

typealias GoToGuide = () -> Unit

fun interface GoToResults {
    operator fun invoke(routeName: String, resultType: ResultType, isJustFound: Boolean, treasureId: Int, amount: Int)
}
typealias GoToFacebook = (String) -> Unit
typealias GoToSearching = (String) -> Unit
typealias GoToQrScanner = () -> Unit
typealias GoToTipPhoto = (String, String) -> Unit
typealias GoToMap = (String) -> Unit

fun interface GoToResultWithTreasure {
    operator fun invoke(treasureId: Int, isJustFound: Boolean)
}
typealias GoToTreasureSelector = (Int) -> Unit

typealias RotatePhoto = (Int) -> Unit
typealias ScanTreasureCallback = (String?) -> Unit
typealias UpdateSubtitlesLine = (String) -> Unit
typealias GoToCommemorative = (Int, String?) -> Unit
typealias GoToTreasureEditor = (String) -> Unit
typealias DoPhoto = () -> Unit

typealias AddTreasure = () -> Unit
typealias RemoveTreasure = (Int) -> Unit
typealias ShowOverridePhotoDialog = (TreasureDescription) -> Unit
typealias HideOverridePhotoDialog = () -> Unit
typealias ShowOverrideSoundTipDialog = (TreasureDescription) -> Unit
typealias HideOverrideSoundTipDialog = () -> Unit
typealias ShowSoundRecordingDialog = (TreasureDescription) -> Unit
typealias HideSoundRecordingDialog = () -> Unit
