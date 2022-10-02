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
import pl.marianjureczko.poszukiwacz.activity.result.ResultActivity
import pl.marianjureczko.poszukiwacz.activity.result.ResultActivityInput
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureContract
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureOutputData
import pl.marianjureczko.poszukiwacz.databinding.ActivitySearchingBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import pl.marianjureczko.poszukiwacz.shared.*

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

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchingBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_searching)
        restoreState()

        binding.scanBtn.setOnClickListener(ScanButtonListener(createScanTreasureLauncher(), resources.getString(R.string.qr_scanner_msg)))
        treasureSelectorLauncher = createSelectTreasureLauncher()
        binding.changeTreasureBtn.setOnClickListener(ChangeTreasureButtonListener(treasureSelectorLauncher, model))
        binding.playTipBtn.setOnClickListener(PlayTipButtonListener(model, this))
        binding.mapBtn.setOnClickListener { errorTone() }
        binding.photoBtn.setOnClickListener(PhotoButtonListener(this, model))

        val locationListener = CompassBasedLocationListener(
            model,
            CompassPresenter(binding.stepsToDo, binding.arrowImg)
        )
        val handler = Handler()
        val locationRequester = LocationRequester(this, locationListener, handler, getSystemService(LOCATION_SERVICE) as LocationManager)
        handler.post(locationRequester)
        setContentView(binding.root)

        setUpAds(binding.adView)
    }

    private fun createScanTreasureLauncher() =
        registerForActivityResult(ScanContract()) { scanResult ->
            if (scanResult != null && scanResult.contents != null) {
                startActivity(ResultActivity.intent(this, processSearchingResult(scanResult.contents)))
            }
        }


    override fun onPostResume() {
        super.onPostResume()
        if (!model.treasureSelectionInitialized()) {
            treasureSelectorLauncher.launch(model.getTreasureSelectorActivityInputData())
        }
    }

    private fun restoreState() {
        model.initialize(intent.getStringExtra(SELECTED_ROUTE)!!, storageHelper)
        showCollectedTreasures()
    }

    private fun processSearchingResult(result: String): ResultActivityInput {
        try {
            val treasure: Treasure = treasureParser.parse(result)
            return if (model.treasureIsAlreadyCollected(treasure)) {
                ResultActivityInput(resources.getString(R.string.treasure_already_taken_msg))
            } else {
                add(treasure)
                ResultActivityInput(treasure)
            }
        } catch (ex: IllegalArgumentException) {
            return ResultActivityInput(resources.getString(R.string.not_a_treasure_msg))
        }
    }

    private fun add(treasure: Treasure) {
        model.collectTreasure(treasure, storageHelper)
        showCollectedTreasures()
    }

    private fun showCollectedTreasures() {
        binding.goldTxt.text = model.getGolds()
        binding.rubyTxt.text = model.getRubies()
        binding.diamondTxt.text = model.getDiamonds()
    }

    private fun createSelectTreasureLauncher(): ActivityResultLauncher<SelectTreasureInputData> {
        return registerForActivityResult(SelectTreasureContract()) { result: SelectTreasureOutputData? ->
            if (result != null) {
                model.replaceTreasureBag(result.progress, storageHelper)
            }
        }
    }
}