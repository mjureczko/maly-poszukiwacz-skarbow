package pl.marianjureczko.poszukiwacz

import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.Composable
import com.journeyapps.barcodescanner.ScanOptions
import org.mockito.BDDMockito.given
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import pl.marianjureczko.poszukiwacz.activity.searching.n.SearchingViewModel
import pl.marianjureczko.poszukiwacz.screen.searching.QrScannerPort
import pl.marianjureczko.poszukiwacz.shared.GoToResults

class TestQrScannerPort : QrScannerPort() {
    var counter: Int = 0

    @Volatile
    private var contents: String = ""

    @Composable
    override fun provideLauncher(
        viewModel: SearchingViewModel,
        goToResult: GoToResults,
    ): ActivityResultLauncher<ScanOptions> {
        val result: ActivityResultLauncher<ScanOptions> = mock()
        given(result.launch(any())).will {
            counter++
            viewModel.scannedTreasureCallback(goToResult)(contents)
        }
        return result
    }

    @Synchronized
    fun getContents(): String {
        return contents
    }

    @Synchronized
    fun setContents(value: String) {
        contents = value
    }
}