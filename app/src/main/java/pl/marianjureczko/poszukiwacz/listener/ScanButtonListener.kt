package pl.marianjureczko.poszukiwacz.listener

import android.view.View
import com.google.zxing.integration.android.IntentIntegrator

class ScanButtonListener(private val qrScan: IntentIntegrator) : View.OnClickListener {
    override fun onClick(v: View?) {
        qrScan.initiateScan()
    }
}