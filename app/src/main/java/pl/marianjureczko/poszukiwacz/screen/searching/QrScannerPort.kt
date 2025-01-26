package pl.marianjureczko.poszukiwacz.screen.searching

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToResults

open class QrScannerPort {

    @Composable
    open fun provideLauncher(
        viewModel: SearchingViewModel,
        goToResult: GoToResults,
    ): ActivityResultLauncher<ScanOptions> {
        return rememberLauncherForActivityResult(
            contract = ScanContract(),
            onResult = { result: ScanIntentResult? ->
                val callback = viewModel.scannedTreasureCallback { routeName, resultType, treasureId, quantity ->
                    goToResult(routeName, resultType, treasureId, quantity)
                }
                callback(result?.contents)
            }
        )
    }
}