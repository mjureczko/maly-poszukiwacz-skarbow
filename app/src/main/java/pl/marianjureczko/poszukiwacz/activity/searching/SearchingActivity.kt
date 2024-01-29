package pl.marianjureczko.poszukiwacz.activity.searching

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.journeyapps.barcodescanner.ScanContract
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.map.MapActivityContract
import pl.marianjureczko.poszukiwacz.activity.map.MapInputData
import pl.marianjureczko.poszukiwacz.activity.result.ResultActivityContract
import pl.marianjureczko.poszukiwacz.activity.result.ResultActivityInput
import pl.marianjureczko.poszukiwacz.activity.result.ResultActivityOutput
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureContract
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureOutputData
import pl.marianjureczko.poszukiwacz.databinding.ActivitySearchingBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.LocationRequester
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class SearchingActivity : ActivityWithAdsAndBackButton() {

    companion object {
        private val xmlHelper = XmlHelper()
        private val treasureParser = TreasureParser()
        private const val SELECTED_ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_selected_to_searching"

        fun intent(packageContext: Context, route: Route) =
            Intent(packageContext, SearchingActivity::class.java).apply {
                putExtra(SELECTED_ROUTE, xmlHelper.writeToString(route))
            }
    }

    private val TAG = javaClass.simpleName
    private val storageHelper = StorageHelper(this)
    private val model: SearchingActivityViewModel by viewModels()
    private lateinit var binding: ActivitySearchingBinding
    private lateinit var treasureSelectorLauncher: ActivityResultLauncher<SelectTreasureInputData>
    private lateinit var showResultLauncher: ActivityResultLauncher<ResultActivityInput>
    private lateinit var showMapLauncher: ActivityResultLauncher<MapInputData>

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_searching)
        restoreState()

        binding.scanBtn.setOnClickListener(
            ScanButtonListener(
                createScanTreasureLauncher(),
                resources.getString(R.string.qr_scanner_msg)
            )
        )
        treasureSelectorLauncher = createSelectTreasureLauncher()
        showResultLauncher = createShowResultLauncher()
        showMapLauncher = createShowMapLauncher()
        binding.changeTreasureBtn.setOnClickListener(ChangeTreasureButtonListener(treasureSelectorLauncher, model))
        binding.playTipBtn.setOnClickListener(PlayTipButtonListener(model, this))
        binding.mapBtn.setOnClickListener(ShowMapButtonListener(showMapLauncher, model))
        binding.photoBtn.setOnClickListener(PhotoButtonListener(this, model))

        val locationListener = CompassBasedLocationListener(
            model,
            CompassPresenter(binding.stepsToDo, binding.arrowImg),
            storageHelper
        )
        val handler = Handler()
        val locationRequester =
            LocationRequester(this, locationListener, handler, getSystemService(LOCATION_SERVICE) as LocationManager)
        handler.post(locationRequester)
        setContentView(binding.root)

        setUpAds(binding.adView)
    }

    override fun getTreasureProgress(): TreasuresProgress? {
        return model.getProgress()
    }

    override fun onPostResume() {
        super.onPostResume()
        if (!model.treasureSelectionInitialized()) {
            treasureSelectorLauncher.launch(model.getTreasureSelectorActivityInputData(null))
        }
    }

    override fun onDestroy() {
        model.releaseMediaPlayer()
        super.onDestroy()
    }

    private fun restoreState() {
        model.initialize(intent.getStringExtra(SELECTED_ROUTE)!!, storageHelper)
        showCollectedTreasures()
    }

    private fun processSearchingResult(result: String): ResultActivityInput {
        return try {
            val treasure: Treasure = treasureParser.parse(result)
            ResultActivityInput(treasure, model.tryToFindTreasureDescription(treasure), model.getTreasuresProgress())
        } catch (ex: IllegalArgumentException) {
            ResultActivityInput(null, null, model.getTreasuresProgress())
        }
    }

    private fun showCollectedTreasures() {
        binding.goldTxt.text = model.getGolds()
        binding.rubyTxt.text = model.getRubies()
        binding.diamondTxt.text = model.getDiamonds()
    }

    private fun createScanTreasureLauncher() =
        registerForActivityResult(ScanContract()) { scanResult ->
            if (scanResult != null && scanResult.contents != null) {
                showResultLauncher.launch(processSearchingResult(scanResult.contents))
                //TODO t: treasure progress need to be reloaded when receiving activity result
            }
        }

    private fun createSelectTreasureLauncher(): ActivityResultLauncher<SelectTreasureInputData> =
        registerForActivityResult(SelectTreasureContract()) { result: SelectTreasureOutputData? ->
            if (result != null) {
                model.replaceProgress(result.progress, storageHelper)
            }
        }

    private fun createShowResultLauncher(): ActivityResultLauncher<ResultActivityInput> =
        registerForActivityResult(ResultActivityContract()) { result: ResultActivityOutput? ->
            result?.let {
                model.loadProgressFromStorage(storageHelper)
                showCollectedTreasures()
                if (it.newTreasureCollected) {
                    treasureSelectorLauncher.launch(model.getTreasureSelectorActivityInputData(it.justFoundTreasureDescription))
                }
            }
        }

    private fun createShowMapLauncher(): ActivityResultLauncher<MapInputData> =
        registerForActivityResult(MapActivityContract()) { }
}