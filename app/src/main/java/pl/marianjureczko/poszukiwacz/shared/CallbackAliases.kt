package pl.marianjureczko.poszukiwacz.shared

import com.journeyapps.barcodescanner.ScanIntentResult
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

typealias GoToGuide = () -> Unit
typealias GoToResults = (ResultType, Int) -> Unit
typealias GoToFacebook = () -> Unit
typealias GoToSearching = (String) -> Unit
typealias GoToQrScanner = () -> Unit
typealias GoToTipPhoto = (String) -> Unit
typealias GoToMap = (String) -> Unit
typealias GoToResultWithTreasure = (Int) -> Unit
typealias GoToTreasureSelector = (Int) -> Unit

typealias RotatePhoto = (Int) -> Unit
typealias ScanTreasureCallback = (ScanIntentResult?) -> Unit
typealias UpdateSubtitlesLine = (String) -> Unit
typealias GoToCommemorative = (Int) -> Unit
typealias GoToTreasureEditor = (String) -> Unit
typealias DoPhoto = () -> Unit

typealias DeleteRoute = () -> Unit
typealias AddTreasure = () -> Unit
typealias RemoveTreasure = (Int) -> Unit
typealias ShowOverridePhotoDialog = () -> Unit
typealias HideOverridePhotoDialog = () -> Unit
typealias ShowOverrideSoundTipDialog = () -> Unit
typealias HideOverrideSoundTipDialog = () -> Unit
typealias ShowSoundRecordingDialog = (TreasureDescription) -> Unit
typealias HideSoundRecordingDialog = () -> Unit
