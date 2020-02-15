package pl.marianjureczko.poszukiwacz

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val MY_PERMISSION_ACCESS_FINE_LOCATION = 12
    private val AMOUNTS = "AMOUNTS"
    private val COLLECTED = "COLLECTED"
    private val MSG_TO_SHOW = "MSG_TO_SHOW"
    private val IMG_TO_SHOW = "IMG_TO_SHOW"

    private var dialog: AlertDialog? = null

    private var locationManager: LocationManager? = null

    private lateinit var qrScan: IntentIntegrator

    private lateinit var treasureBagPresenter: TreasureBagPresenter

    private var dialogToShow: DialogData? = null

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

    override fun onStart() {
        println("########> onStart ${System.currentTimeMillis() % 100_000}")
        super.onStart()
    }

    override fun onResume() {
        println("########> onResume ${System.currentTimeMillis() % 100_000}")
        super.onResume()
    }

    override fun onPause() {
        println("########> onPause ${System.currentTimeMillis() % 100_000}")
        super.onPause()
    }

    override fun onStop() {
        println("########> onStop ${System.currentTimeMillis() % 100_000}")
        super.onStop()
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
            putIntegerArrayList(AMOUNTS, treasureBagPresenter.bagContent())
            putStringArrayList(COLLECTED, treasureBagPresenter.collectedInBag())
            println("########> onSaveInstanceState dialog:$dialogToShow")
            putString(MSG_TO_SHOW, dialogToShow?.msg)
            if (dialogToShow?.imageId != null) {
                putInt(IMG_TO_SHOW, dialogToShow?.imageId!!)
            }
        }
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("########> onCreate ${System.currentTimeMillis() % 100_000}")
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        restoreState(savedInstanceState)

        qrScan = IntentIntegrator(this)
        scanBtn.setOnClickListener(this)

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        val locationListener = MyLocationListener(findViewById(R.id.latValue), findViewById(R.id.longValue))
        val handler = Handler()
        val context: Context = this
        val activity: Activity = this

        val runnableCode: Runnable = object : Runnable {
            override fun run() {
                if (ContextCompat.checkSelfPermission(
                        context,
                        ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        1000L,
                        1.0F,
                        locationListener
                    )
                    handler.postDelayed(this, 1000L)
                } else {
                    ActivityCompat.requestPermissions(
                        activity, arrayOf(ACCESS_FINE_LOCATION), MY_PERMISSION_ACCESS_FINE_LOCATION
                    )
                }

            }
        }
        handler.post(runnableCode)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        treasureBagPresenter = TreasureBagPresenter(
            savedInstanceState?.getIntegerArrayList(AMOUNTS),
            savedInstanceState?.getStringArrayList(COLLECTED)
        )
        treasureBagPresenter.init(
            findViewById(R.id.goldTxt),
            findViewById(R.id.rubyTxt),
            findViewById(R.id.diamondTxt)
        )
        treasureBagPresenter.showTreasure()
        savedInstanceState?.getString(MSG_TO_SHOW)?.let {
            dialogToShow = DialogData(it, savedInstanceState?.getInt(IMG_TO_SHOW))
        }
        println("########> restoreState dialog:$dialogToShow")
    }

    override fun onClick(view: View) {
        onActivityResult(1, 1, null)
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        println("########> onActivityResult ${System.currentTimeMillis() % 100_000}")
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            dialogToShow = treasureBagPresenter.processSearchingResult(result.contents, SearchResultDialog(this))
            println("########> onActivityResult (done) dialog:$dialogToShow ${System.currentTimeMillis() % 100_000}")
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}

class MyLocationListener(private val latValue: TextView, private val longValue: TextView) : LocationListener {

    private val formatter = CoordinatesFormatter()

    override fun onLocationChanged(location: Location?) {
        latValue.text = formatter.format(location?.latitude)
        longValue.text = formatter.format(location?.longitude)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        println("onStatusChanged - provider: $provider")
    }

    override fun onProviderEnabled(provider: String?) {
        println("onProviderEnabled - provider: $provider")
    }

    override fun onProviderDisabled(provider: String?) {
        println("onProviderDisabled - provider: $provider")
    }

}
