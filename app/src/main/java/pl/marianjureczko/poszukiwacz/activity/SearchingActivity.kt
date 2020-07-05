package pl.marianjureczko.poszukiwacz.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_searching.*
import pl.marianjureczko.poszukiwacz.*
import pl.marianjureczko.poszukiwacz.dialog.SearchResultDialog
import pl.marianjureczko.poszukiwacz.dialog.TreasureSelectionDialog
import pl.marianjureczko.poszukiwacz.listener.ChangeTreasureButtonListener
import pl.marianjureczko.poszukiwacz.listener.ScanButtonListener
import pl.marianjureczko.poszukiwacz.listener.TextViewBasedLocationListener

interface TreasureLocationView {
    fun showTreasureLocation(which: Int)
}

interface TreasureSelectorView {
    fun selectTreasureForSearching()
}

private const val LOG_TAG = "SearchingActivity"

class SearchingActivity : AppCompatActivity(), TreasureLocationView, TreasureSelectorView {
    private val AMOUNTS = "AMOUNTS"
    private val COLLECTED = "COLLECTED"
    private val MSG_TO_SHOW = "MSG_TO_SHOW"
    private val IMG_TO_SHOW = "IMG_TO_SHOW"
    private val xmlHelper = XmlHelper()
    private val formatter = CoordinatesFormatter()

    private var dialog: AlertDialog? = null
    private lateinit var qrScan: IntentIntegrator

    companion object {
        private var treasureBagPresenter: TreasureBagPresenter? = null
        private var treasuresList: TreasuresList? = null
        private var selectedTreasure: Int = 0
    }

    private var dialogToShow: DialogData? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        println("########> onCreate ${System.currentTimeMillis() % 100_000}")
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_searching)

        restoreState(savedInstanceState)

        qrScan = IntentIntegrator(this)
        scanBtn.setOnClickListener(ScanButtonListener(qrScan))

        val locationListener =
            TextViewBasedLocationListener(
                findViewById(R.id.latValue),
                findViewById(R.id.longValue)
            )
        val handler = Handler()
        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = LocationPresenter(this, locationListener, handler, this, location)
        handler.post(presenter)
        treasuresList = xmlHelper.loadFromString(intent.getStringExtra(MainActivity.SELECTED_LIST))
        selectTreasureForSearching()
        changeTreasureBtn.setOnClickListener(ChangeTreasureButtonListener(this))
        playTipBtn.setOnClickListener() { _ ->
            MediaPlayer().apply {
                try {
                    treasuresList?.treasures?.get(selectedTreasure)?.tipFileName?.let {
                        setDataSource(it)
                        prepare()
                        start()
                        Thread.sleep(3000)
                    }
                } catch (e: Exception) {
                    Log.e(LOG_TAG, "Cannot play the treasure tip.")
                }
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        println("########> onPostResume ${System.currentTimeMillis() % 100_000}")
        val toShow = dialogToShow
        if (toShow != null) {
            try {
                dialog = SearchResultDialog(this).show(toShow.msg, toShow.imageId)
                println("########> onPostResume ${System.currentTimeMillis() % 100_000} setting null")
                dialogToShow = null
            } catch (ex: Throwable) {
                dialogToShow = toShow
            }
        }
    }

    override fun onDestroy() {
        println("########> onDestroy ${System.currentTimeMillis() % 100_000}")
        super.onDestroy()
        dialog?.dismiss()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        println("########> onRestoreInstanceState ${System.currentTimeMillis() % 100_000}")
        restoreState(savedInstanceState)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        println("########> onSaveInstanceState ${System.currentTimeMillis() % 100_000}")
        outState?.run {
            putIntegerArrayList(AMOUNTS, treasureBagPresenter!!.bagContent())
            putStringArrayList(COLLECTED, treasureBagPresenter!!.collectedInBag())
            println("########> onSaveInstanceState dialog:$dialogToShow")
            putString(MSG_TO_SHOW, dialogToShow?.msg)
            if (dialogToShow?.imageId != null) {
                putInt(IMG_TO_SHOW, dialogToShow?.imageId!!)
            }
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun selectTreasureForSearching() {
        TreasureSelectionDialog(this, this).show(treasuresList!!)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        if (treasureBagPresenter == null) {
            treasureBagPresenter =
                TreasureBagPresenter(
                    savedInstanceState?.getIntegerArrayList(AMOUNTS),
                    savedInstanceState?.getStringArrayList(COLLECTED)
                )
        }
        treasureBagPresenter!!.init(
            findViewById(R.id.goldTxt),
            findViewById(R.id.rubyTxt),
            findViewById(R.id.diamondTxt)
        )
        treasureBagPresenter!!.showTreasure()
        savedInstanceState?.getString(MSG_TO_SHOW)?.let {
            dialogToShow = DialogData(it, savedInstanceState?.getInt(IMG_TO_SHOW))
        }
        println("########> restoreState dialog:$dialogToShow")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("########> onActivityResult ${System.currentTimeMillis() % 100_000}")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null && result.contents != null) {
            dialogToShow = treasureBagPresenter!!.processSearchingResult(
                result.contents,
                SearchResultDialog(this)
            )
            println("########> onActivityResult (done) dialog:$dialogToShow ${System.currentTimeMillis() % 100_000}")
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun showTreasureLocation(which: Int) {
        selectedTreasure = which
        val treasure = treasuresList!!.treasures[selectedTreasure]
        val latitude = findViewById<TextView>(R.id.latTarget)
        latitude.text = formatter.format(treasure.latitude)
        val longitude = findViewById<TextView>(R.id.longTarget)
        longitude.text = formatter.format(treasure.longitude)
    }

}