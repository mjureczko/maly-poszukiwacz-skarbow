package pl.marianjureczko.poszukiwacz.shared

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.*
import pl.marianjureczko.poszukiwacz.R

abstract class ActivityWithAdsAndBackButton : AppCompatActivity() {

    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.chest_very_small)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    protected fun setUpAds(mAdView: AdView) {
        //TODO: set max_ad_content_rating to PG or G on https://apps.admob.com
        var requestConfiguration = MobileAds.getRequestConfiguration()
            .toBuilder()
            .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
            .build()
        MobileAds.setRequestConfiguration(requestConfiguration)

        MobileAds.initialize(this) { Log.d(TAG, "Ads initialized")}

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object: AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError : LoadAdError) {
                Toast.makeText(this@ActivityWithAdsAndBackButton, adError.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onAdOpened() {
            }

            override fun onAdClicked() {
            }

            override fun onAdClosed() {
            }
        }

    }
}