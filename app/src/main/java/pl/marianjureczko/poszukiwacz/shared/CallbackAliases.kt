package pl.marianjureczko.poszukiwacz.shared

import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.screen.result.ResultType

typealias GoToGuide = () -> Unit
typealias GoToResults = (String, ResultType, Int, Int) -> Unit
typealias GoToFacebook = (String) -> Unit
typealias GoToSearching = (String) -> Unit
typealias GoToQrScanner = () -> Unit
typealias GoToTipPhoto = (String, String) -> Unit
typealias GoToMap = (String) -> Unit
typealias GoToResultWithTreasure = (Int) -> Unit
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
