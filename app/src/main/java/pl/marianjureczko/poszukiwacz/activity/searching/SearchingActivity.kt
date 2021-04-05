package pl.marianjureczko.poszukiwacz.activity.searching

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_searching.*
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.shared.LocationRequester
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

private const val RESULTS_DIALOG = "ResultsDialog"

class SearchingActivity : AppCompatActivity(), TreasureSelectorView, DialogCleaner {

    companion object {
        private var treasureBagPresenter: TreasureBagPresenter? = null
        private val xmlHelper = XmlHelper()
        private const val SELECTED_ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_selected_to_searching";

        fun intent(packageContext: Context, route: Route) =
            Intent(packageContext, SearchingActivity::class.java).apply {
                putExtra(SELECTED_ROUTE, xmlHelper.writeToString(route))
            }
    }

    private val TAG = javaClass.simpleName
    private val AMOUNTS_KEY = "AMOUNTS"
    private val COLLECTED_KEY = "COLLECTED"
    private val MSG_TO_SHOW_KEY = "MSG_TO_SHOW"
    private val IMG_TO_SHOW_KEY = "IMG_TO_SHOW"
    private val SELECTED_ROUTE_KEY = "ROUTE"
    private val SELECTED_TREASURE_INDEX_KEY = "TREASURE"

    // When not null, a dialog should be shown on postResume
//    private var dialogToShow: DialogData? = null

    // Is set when the dialog is visible on screen
    private var dialog: AlertDialog? = null

    private val model: SearchingActivityViewModel by viewModels()

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "########> onCreate")
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_searching)
        restoreState(savedInstanceState)

        scanBtn.setOnClickListener(ScanButtonListener(IntentIntegrator(this)))
        changeTreasureBtn.setOnClickListener(ChangeTreasureButtonListener(this))
        playTipBtn.setOnClickListener(PlayTipButtonListener(model))
        mapBtn.setOnClickListener { ToneGenerator(AudioManager.STREAM_NOTIFICATION, 50).startTone(ToneGenerator.TONE_PROP_BEEP) }

        val locationListener = CompassBasedLocationListener(
            model,
            CompassPresenter(findViewById(R.id.stepsToDo), findViewById(R.id.arrowImg))
        )
        val handler = Handler()
        val locationRequester = LocationRequester(this, locationListener, handler, getSystemService(LOCATION_SERVICE) as LocationManager)
        handler.post(locationRequester)
    }

//    override fun onPostResume() {
//        super.onPostResume()
//        Log.d(TAG, "########> onPostResume")
//        if (dialogToShow != null && dialog == null) {
//            dialog = SearchResultDialog(this, this).show(dialogToShow!!)
//        } else if (model.selectedTreasure == null) {
//            showTreasureSelectionDialog()
//        }
//    }

    override fun onDestroy() {
        Log.d(TAG, "########> onDestroy")
        super.onDestroy()
        dialog?.dismiss()
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "########> onSaveInstanceState")
        outState.run {
            putString(SELECTED_ROUTE_KEY, model.routeXml)
            model.treasureIndex?.let { putInt(SELECTED_TREASURE_INDEX_KEY, it) }
            putIntegerArrayList(AMOUNTS_KEY, treasureBagPresenter!!.bagContent())
            putStringArrayList(COLLECTED_KEY, treasureBagPresenter!!.collectedInBag())
//            putString(MSG_TO_SHOW_KEY, dialogToShow?.msg)
//            if (dialogToShow?.imageId != null) {
//                putInt(IMG_TO_SHOW_KEY, dialogToShow?.imageId!!)
//            }
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun showTreasureSelectionDialog() {
        TreasureSelectionDialog(this, model).show(model.route)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (model.routeXml == null) {
            model.setup(intent.getStringExtra(SELECTED_ROUTE))
        }
        savedInstanceState?.getString(SELECTED_ROUTE_KEY)?.let { model.setup(it) }
        savedInstanceState?.getInt(SELECTED_TREASURE_INDEX_KEY)?.let { model.selectTreasure(it) }
        if (treasureBagPresenter == null) {
            treasureBagPresenter = TreasureBagPresenter(
                savedInstanceState?.getIntegerArrayList(AMOUNTS_KEY),
                savedInstanceState?.getStringArrayList(COLLECTED_KEY)
            )
        }
        treasureBagPresenter!!.init(findViewById(R.id.goldTxt), findViewById(R.id.rubyTxt), findViewById(R.id.diamondTxt))
        treasureBagPresenter!!.showCollectedTreasures()
//        savedInstanceState?.getString(MSG_TO_SHOW_KEY)?.let {
//            dialogToShow = DialogData(it, savedInstanceState.getInt(IMG_TO_SHOW_KEY))
//        }
    }

    /** Result of scanning treasure qr code*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "########> onActivityResult")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            var dialogToShow = treasureBagPresenter!!.processSearchingResult(result.contents)
            SearchResultDialog.newInstance(dialogToShow).apply {
                show(this@SearchingActivity.supportFragmentManager, RESULTS_DIALOG)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun cleanupAfterDialog() {
//        dialogToShow = null
//        dialog = null
    }
}