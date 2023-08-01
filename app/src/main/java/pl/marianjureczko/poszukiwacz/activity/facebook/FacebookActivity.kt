package pl.marianjureczko.poszukiwacz.activity.facebook

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.share.Sharer
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityFacebookBinding
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

class FacebookActivity : ActivityWithAdsAndBackButton() {

    private lateinit var binding: ActivityFacebookBinding
    private lateinit var adapter: ElementsAdapter
    private lateinit var shareDialog: ShareDialog
    private lateinit var callbackManager: CallbackManager
    private val model: FacebookViewModel by viewModels()

    companion object {
        const val TREASURE_PROGRESS = "pl.marianjureczko.poszukiwacz.activity.facebook_treasure_progress"
        private val xmlHelper = XmlHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        model.initialize(
            progress = xmlHelper.loadFromString(intent.getStringExtra(TREASURE_PROGRESS)!!),
        )
        //TODO: deprecated
        FacebookSdk.sdkInitialize(applicationContext)
        callbackManager = CallbackManager.Factory.create()
        shareDialog = ShareDialog(this)

        binding = ActivityFacebookBinding.inflate(layoutInflater)
        binding.elements.layoutManager = LinearLayoutManager(this)


        adapter = ElementsAdapter(this, model)
        binding.elements.adapter = adapter
        binding.shareOnFacebook.setOnClickListener {

            val reportImage = ReportGenerator().create(this, model)
            shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
                override fun onSuccess(result: Sharer.Result) = Toast.makeText(this@FacebookActivity, getString(R.string.facebook_share_success), Toast.LENGTH_SHORT).show()
                override fun onCancel() = Toast.makeText(this@FacebookActivity, getString(R.string.facebook_share_cancel), Toast.LENGTH_SHORT).show()
                override fun onError(error: FacebookException) =
                    Toast.makeText(this@FacebookActivity, getString(R.string.facebook_share_error) + error.localizedMessage, Toast.LENGTH_LONG).show()
            })
            val sharePhoto = SharePhoto.Builder()
                .setBitmap(reportImage)
                .build()
            if (ShareDialog.canShow(SharePhotoContent::class.java)) {
                var sharePhotoContent = SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build()
                shareDialog.show(sharePhotoContent)
            } else {
                Toast.makeText(this, getString(R.string.facebook_share_impossible), Toast.LENGTH_SHORT).show()
            }
        }

        setContentView(binding.root)
        setUpAds(binding.adView)
    }

    override fun getCurrentTreasuresProgress(): TreasuresProgress? = null
}