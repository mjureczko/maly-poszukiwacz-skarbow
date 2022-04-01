package pl.marianjureczko.poszukiwacz.activity.searching

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import com.google.zxing.integration.android.IntentIntegrator
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureContract
import pl.marianjureczko.poszukiwacz.activity.treasureselector.SelectTreasureInputData
import pl.marianjureczko.poszukiwacz.activity.treasureselector.TreasureSelectorActivity
import pl.marianjureczko.poszukiwacz.databinding.ActivitySearchingBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.Treasure
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureParser
import pl.marianjureczko.poszukiwacz.shared.*

private const val RESULTS_DIALOG = "ResultsDialog"

class SearchingActivity : ActivityWithAdsAndBackButton() {

    companion object {
        private val xmlHelper = XmlHelper()
        private val treasureParser = TreasureParser()
        private val SELECTED_ROUTE_KEY = "ROUTE"
        private val SELECTED_TREASURE_INDEX_KEY = "TREASURE"
        private val INITIALIZED_KEY = "INITIALIZED"
        private const val SELECTED_ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_selected_to_searching";

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
        restoreState(savedInstanceState)

        binding.scanBtn.setOnClickListener(ScanButtonListener(IntentIntegrator(this)))
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

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "########> onSaveInstanceState")
        outState.run {
            putString(SELECTED_ROUTE_KEY, model.routeXml)
            model.treasureIndex?.let { putInt(SELECTED_TREASURE_INDEX_KEY, it) }
            storageHelper.save(model.treasureBag)
            putBoolean(INITIALIZED_KEY, model.treasureSelectionInitialized)
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onPostResume() {
        Log.d(TAG, "########> onPostResume")
        super.onPostResume()
        if (!model.treasureSelectionInitialized) {
            model.treasureSelectionInitialized = true
            treasureSelectorLauncher.launch(model.getTreasureSelectorActivityInputData())
        }
    }

    /** Result of scanning treasure qr code*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "########> onActivityResult")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            var dialogToShow = processSearchingResult(result.contents)
            SearchResultDialog.newInstance(dialogToShow).apply {
                show(this@SearchingActivity.supportFragmentManager, RESULTS_DIALOG)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (model.routeXml == null) {
            model.setup(intent.getStringExtra(SELECTED_ROUTE)!!)
        }
        savedInstanceState?.getString(SELECTED_ROUTE_KEY)?.let { model.setup(it) }
        savedInstanceState?.getInt(SELECTED_TREASURE_INDEX_KEY)?.let { model.selectTreasure(it) }
        savedInstanceState?.getBoolean(INITIALIZED_KEY)?.let { model.treasureSelectionInitialized = it }
        model.treasureBag = storageHelper.load(model.route.name) ?: TreasureBag(model.route.name)
        showCollectedTreasures()
    }

    private fun processSearchingResult(result: String): DialogData {
        try {
            val treasure = treasureParser.parse(result)
            return if (model.treasureBag!!.contains(treasure)) {
                DialogData(resources.getString(R.string.treasure_already_taken_msg), null)
            } else {
                add(treasure)
                DialogData(treasure.quantity.toString(), treasure.type.image())
            }
        } catch (ex: IllegalArgumentException) {
            return DialogData(resources.getString(R.string.not_a_treasure_msg), null)
        }
    }

    private fun add(treasure: Treasure) {
        model.treasureBag.collect(treasure)
        showCollectedTreasures()
    }

    private fun showCollectedTreasures() {
        binding.goldTxt.text = model.treasureBag.golds.toString()
        binding.rubyTxt.text = model.treasureBag.rubies.toString()
        binding.diamondTxt.text = model.treasureBag.diamonds.toString()
    }

    private fun createSelectTreasureLauncher(): ActivityResultLauncher<SelectTreasureInputData> {
        return registerForActivityResult(SelectTreasureContract()) { selectedTreasureId: Int? ->
            selectedTreasureId?.let {
                if(selectedTreasureId != TreasureSelectorActivity.NON_SELECTED) {
                    model.selectTreasure(it)
                }
            }
        }
    }
}