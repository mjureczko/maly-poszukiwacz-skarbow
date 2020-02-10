package pl.marianjureczko.poszukiwacz

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.*
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

    private var locationManager: LocationManager? = null

    private lateinit var qrScan: IntentIntegrator

    private val treasureBagPresenter = TreasureBagPresenter()
    private val treasureParser = TreasureParser()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        qrScan = IntentIntegrator(this)
        scanBtn.setOnClickListener(this)

        val latValue = findViewById<TextView>(R.id.latValue)
        latValue.text = "changed"

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager


        val locationListener = MyLocationListener()
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

    override fun onClick(view: View) {
        onActivityResult(1, 1, null)
        qrScan.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            val resultDialog = SearchResultDialog(this)
            try {
                val treasure = treasureParser.parse(result.contents)
                if(treasureBagPresenter.contains(treasure)) {
                    resultDialog.show("Ten skarb został już zabrany!", null)
                } else {
                    resultDialog.show(treasure.quantity.toString(), treasure.type.image())
                    treasureBagPresenter.add(treasure)
                }
            } catch (ex: IllegalArgumentException) {
                resultDialog.show("To nie jest skarb!", null)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}

class MyLocationListener : LocationListener {
    override fun onLocationChanged(location: Location?) {
//        println("szerokość: ${location?.latitude} długość: ${location?.longitude}")
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


object DrawableUtil {

    fun writeTextOnDrawableInternal(
        bm: Bitmap,
        text: String,
        textSizeDp: Int,
        horizontalOffset: Int,
        verticalOffset: Int
    ): Unit {

        val tf = Typeface.create("Helvetica", Typeface.BOLD)

        val paint = Paint()
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        paint.typeface = tf
        paint.textAlign = Paint.Align.LEFT
//        paint.textSize = context.dip(textSizeDp).toFloat()

        val textRect = Rect()
        paint.getTextBounds(text, 0, text.length, textRect)

        val canvas = Canvas(bm)

//        //If the text is bigger than the canvas , reduce the font size
//        if (textRect.width() >= canvas.getWidth() - 4)
//        //the padding on either sides is considered as 4, so as to appropriately fit in the text
//            paint.textSize = context.dip(12).toFloat()

        //Calculate the positions
        val xPos = canvas.width.toFloat() / 2 + horizontalOffset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        val yPos = (canvas.height / 2 - (paint.descent() + paint.ascent()) / 2) + verticalOffset

        canvas.drawText(text, xPos, yPos, paint)
    }
}