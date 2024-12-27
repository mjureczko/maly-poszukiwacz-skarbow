package pl.marianjureczko.poszukiwacz.shared

import com.journeyapps.barcodescanner.ScanIntentResult
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType
import pl.marianjureczko.poszukiwacz.model.TreasureDescription

typealias GoToGuide = () -> Unit
//TODO t: are nullable types supported in navigation?
typealias GoToResults = (ResultType, Int?, Int?) -> Unit
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

//TODO t: use or remove
typealias DeleteRoute = () -> Unit
typealias AddTreasure = () -> Unit
typealias RemoveTreasure = (Int) -> Unit
typealias ShowOverridePhotoDialog = (TreasureDescription) -> Unit
typealias HideOverridePhotoDialog = () -> Unit
typealias ShowOverrideSoundTipDialog = (TreasureDescription) -> Unit
typealias HideOverrideSoundTipDialog = () -> Unit
typealias ShowSoundRecordingDialog = (TreasureDescription) -> Unit
typealias HideSoundRecordingDialog = () -> Unit
