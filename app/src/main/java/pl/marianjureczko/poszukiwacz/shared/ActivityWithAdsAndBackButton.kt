package pl.marianjureczko.poszukiwacz.shared

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import com.google.android.gms.ads.*
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress


abstract class ActivityWithAdsAndBackButton : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private lateinit var callbackManager: CallbackManager
    private lateinit var shareDialog: ShareDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //TODO: deprecated
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.chest_very_small)
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId

        if (id == R.id.helpbutton) {
            val url = this.getString(R.string.help_path)
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        } else if (id == R.id.facebook) {
            shareImage()
        }
        return super.onOptionsItemSelected(item)
    }

    protected abstract fun getCurrentTreasuresProgress(): TreasuresProgress?
    private fun shareImage() {
        val progress = getCurrentTreasuresProgress()
        if (progress != null) {
            shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
                override fun onSuccess(result: Sharer.Result) {
                    // Handle successful sharing
                    println("Share successful")
                }

                override fun onCancel() {
                    // Handle sharing cancellation
                    println("Share cancelled")
                }

                override fun onError(error: FacebookException) {
                    // Handle sharing error
                    println("Share error: ${error.message}")
                }
            })
            val image = FacebookReport().create(this, progress)
            val sharePhoto = SharePhoto.Builder()
                .setBitmap(image)
                .build()
            if (ShareDialog.canShow(SharePhotoContent::class.java)) {
                var sharePhotoContent = SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build()
                shareDialog.show(sharePhotoContent)
            }
            //TODO: else
        } else {
            errorTone()
        }
//        val bitmap = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(bitmap)
//        canvas.drawColor(Color.YELLOW)
//
//        // Load the desired font from file
//        val fontFile = File("@font/akaya_telivigala")
//        val typeface = Typeface.createFromFile(fontFile)
//
//        val paint = Paint().apply {
//            color = Color.BLACK
//            textSize = 16f
//            //typeface = typeface // Set the desired font
//        }
//        canvas.drawText("Hello, World!", 100f, 100f, paint)
//
//        val newPhotoFile = StorageHelper(this).newPhotoFile()
//        val imageFile = File(newPhotoFile)
//
//        try {
//            val stream = FileOutputStream(imageFile)
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//            stream.flush()
//            stream.close()
//            MediaScannerConnection.scanFile(this, arrayOf(imageFile.absolutePath), null, null)
//        } catch (e: IOException) {
//            e.printStackTrace()
//        }
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

        MobileAds.initialize(this) { Log.d(TAG, "Ads initialized") }

        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        mAdView.adListener = object : AdListener() {
            override fun onAdLoaded() {
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
//                Toast.makeText(this@ActivityWithAdsAndBackButton, adError.toString(), Toast.LENGTH_SHORT).show()
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