package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R

@Composable
fun AdvertBanner(){
    AndroidView(
        modifier = Modifier.fillMaxWidth(),
        factory = { context ->
            AdView(App.getAppContext()).apply {
                adSize = AdSize.BANNER
                adUnitId = App.getResources().getString(R.string.main_ad)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}