//package pl.marianjureczko.poszukiwacz.shared
//
//import android.annotation.SuppressLint
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.util.Log
//import android.view.Menu
//import android.view.MenuItem
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.appcompat.app.AppCompatActivity
//import androidx.appcompat.view.menu.MenuBuilder
//import com.facebook.CallbackManager
//import com.facebook.share.widget.ShareDialog
//import com.google.android.gms.ads.*
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookContract
//import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData
//import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookOutputData
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//
//
//abstract class ActivityWithAdsAndBackButton : AppCompatActivity(), SelectTreasureProgressDialog.Callback {
//
//    private val TAG = javaClass.simpleName
//    private lateinit var callbackManager: CallbackManager
//    private lateinit var shareDialog: ShareDialog
//
//    private lateinit var facebookLauncher: ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData>
//    private val storageHelper: StorageHelper by lazy { StorageHelper(this) }
//    /** Initialized to be used by childs in onCreate()*/
//    protected lateinit var settings: Settings
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        settings = Settings(assets)
//
//        callbackManager = CallbackManager.Factory.create()
//        shareDialog = ShareDialog(this)
//
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setIcon(R.drawable.chest_very_small)
//
//        facebookLauncher = createShareOnFacebookLauncher()
//    }
//
//    @SuppressLint("RestrictedApi")
//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.menu_main, menu)
//        if (menu is MenuBuilder) {
//            menu.setOptionalIconsVisible(true)
//        }
//        return super.onCreateOptionsMenu(menu)
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val id: Int = item.itemId
//
//        if (id == R.id.helpbutton) {
//            val url = this.getString(R.string.help_path)
//            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
//        } else if (id == R.id.facebook) {
//            if (getTreasureProgress() != null) {
//                facebookLauncher.launch(createFacebookInputData(getTreasureProgress()!!))
//            } else {
//                val progresses = storageHelper.loadAll()
//                    .mapNotNull { route -> storageHelper.loadProgress(route.name) }
//                    .toList()
//                if (progresses.isEmpty()) {
//                    Toast.makeText(this, R.string.facebook_nothing_to_share, Toast.LENGTH_SHORT).show()
//                } else {
//                    if (progresses.size == 1) {
//                        facebookLauncher.launch(createFacebookInputData(progresses[0]))
//                    } else {
//                        SelectTreasureProgressDialog.newInstance(progresses).apply {
//                            show(supportFragmentManager, "SelectTreasureProgressDialog")
//                        }
//                    }
//                }
//            }
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//    private fun createFacebookInputData(treasureProgress: TreasuresProgress): pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData {
//        return pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData(
//            storageHelper.loadHunterPath(
//                treasureProgress.routeName
//            ), treasureProgress
//        )
//    }
//
//    override fun onTreasureProgressSelected(routeName: String) {
//        val progresses = storageHelper.loadProgress(routeName)
//        if (progresses == null) {
//            Toast.makeText(this, R.string.facebook_invalid_roote, Toast.LENGTH_SHORT).show()
//        } else {
//            facebookLauncher.launch(createFacebookInputData(progresses))
//        }
//    }
//
//    protected abstract fun getTreasureProgress(): TreasuresProgress?
//
//    private fun createShareOnFacebookLauncher(): ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData> =
//        registerForActivityResult(pl.marianjureczko.poszukiwacz.activity.facebook.FacebookContract()) { result: pl.marianjureczko.poszukiwacz.activity.facebook.FacebookOutputData? ->
//            Log.d(TAG, "Sharing on facebook result: ${result?.result}")
//        }
//
//    override fun onSupportNavigateUp(): Boolean {
//        onBackPressed()
//        return true
//    }
//
//    protected fun setUpAds(mAdView: AdView) {
//        //TODO: set max_ad_content_rating to PG or G on https://apps.admob.com
//        var requestConfiguration = MobileAds.getRequestConfiguration()
//            .toBuilder()
//            .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
//            .setMaxAdContentRating(RequestConfiguration.MAX_AD_CONTENT_RATING_G)
//            .build()
//        MobileAds.setRequestConfiguration(requestConfiguration)
//
//        try {
//            MobileAds.initialize(this) { Log.d(TAG, "Ads initialized") }
//        } catch (e: Exception) {
//            Log.e(TAG, "Ads initialization error: ${e.message}")
//        }
//
//        val adRequest = AdRequest.Builder().build()
//        mAdView.loadAd(adRequest)
//
//        mAdView.adListener = object : AdListener() {
//            override fun onAdLoaded() {
//            }
//
//            override fun onAdFailedToLoad(adError: LoadAdError) {
//            }
//
//            override fun onAdOpened() {
//            }
//
//            override fun onAdClicked() {
//            }
//
//            override fun onAdClosed() {
//            }
//        }
//
//    }
//}