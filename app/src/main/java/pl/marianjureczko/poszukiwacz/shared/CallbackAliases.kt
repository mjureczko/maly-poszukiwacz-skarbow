package pl.marianjureczko.poszukiwacz.shared

import com.journeyapps.barcodescanner.ScanIntentResult
import pl.marianjureczko.poszukiwacz.activity.result.n.ResultType

typealias GoToGuide = () -> Unit
typealias GoToResults = (ResultType, Int) -> Unit
typealias GoToFacebook = () -> Unit
typealias GoToSearching = (String) -> Unit
typealias GoToQrScanner = () -> Unit
typealias GoToTipPhoto = (String) -> Unit
typealias GoToMap = (String) -> Unit
typealias GoToResultWithTreasure = (Int) -> Unit
typealias GoToTreasureSelector = (Int) -> Unit

typealias DeleteRoute = () -> Unit
typealias DoPhotoResultHandler = () -> Unit
typealias RotatePhoto = (String) -> Unit
typealias ScanTreasureCallback = (ScanIntentResult?) -> Unit
typealias UpdateSubtitlesLine = (String) -> Unit
typealias GoToCommemorative = (Int) -> Unit