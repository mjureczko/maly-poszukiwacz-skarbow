package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import pl.marianjureczko.poszukiwacz.R

@Composable
fun AdvertBanner(){
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            MobileAds.setRequestConfiguration(
                MobileAds.getRequestConfiguration()
                    .toBuilder()
                    .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                    .setTagForUnderAgeOfConsent(RequestConfiguration.TAG_FOR_UNDER_AGE_OF_CONSENT_TRUE)
                    .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
                    .build()
            )

            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = context.resources.getString(R.string.main_ad)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}