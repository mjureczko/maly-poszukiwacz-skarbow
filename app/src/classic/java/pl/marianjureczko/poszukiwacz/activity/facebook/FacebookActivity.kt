//package pl.marianjureczko.poszukiwacz.activity.facebook
//
//import android.app.AlertDialog
//import android.content.pm.ActivityInfo
//import android.graphics.Bitmap
//import android.os.Build
//import android.os.Bundle
//import android.util.Log
//import android.widget.Toast
//import androidx.activity.viewModels
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.facebook.CallbackManager
//import com.facebook.FacebookCallback
//import com.facebook.FacebookException
//import com.facebook.share.Sharer
//import com.facebook.share.model.SharePhoto
//import com.facebook.share.model.SharePhotoContent
//import com.facebook.share.widget.ShareDialog
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.databinding.ActivityFacebookBinding
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
//import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
//import pl.marianjureczko.poszukiwacz.permissions.RequirementsForExternalStorage
//import pl.marianjureczko.poszukiwacz.shared.ExternalStorage
//import pl.marianjureczko.poszukiwacz.shared.XmlHelper
//
//class FacebookActivity : PermissionActivity() {
//
//    private val TAG = javaClass.simpleName
//    private lateinit var binding: ActivityFacebookBinding
//    private lateinit var adapter: ElementsAdapter
//    private lateinit var shareDialog: ShareDialog
//    private lateinit var callbackManager: CallbackManager
//    private val model: FacebookViewModel by viewModels()
//
//    companion object {
//        const val INPUT = "pl.marianjureczko.poszukiwacz.activity.input"
//        const val STORAGE_DO_NOT_REQUIRE_PERMISSONS = Build.VERSION_CODES.Q
//        private val xmlHelper = XmlHelper()
//    }
//
//    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {
//        //do nothing
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        val input = intent.getSerializableExtra(INPUT) as FacebookInputData
//        model.initialize(this, hunterPath = input.hunterPath, progress = input.progress)
//        callbackManager = CallbackManager.Factory.create()
//        shareDialog = ShareDialog(this)
//
//        binding = ActivityFacebookBinding.inflate(layoutInflater)
//        binding.elements.layoutManager = LinearLayoutManager(this)
//
//
//        adapter = ElementsAdapter(this, model)
//        binding.elements.adapter = adapter
//        binding.shareOnFacebook.setOnClickListener {
//            pl.marianjureczko.poszukiwacz.activity.facebook.n.ReportGenerator().create(this, model) {
//                forwardReportToFacebook(it)
//            }
//        }
//
//        setContentView(binding.root)
//        assurePermissionsAreGranted(RequirementsForExternalStorage, true)
//        setUpAds(binding.adView)
//    }
//
//    private fun forwardReportToFacebook(reportImage: Bitmap) {
//        shareDialog.registerCallback(callbackManager, object : FacebookCallback<Sharer.Result> {
//            override fun onSuccess(result: Sharer.Result) =
//                Toast.makeText(this@FacebookActivity, getString(R.string.facebook_share_success), Toast.LENGTH_SHORT)
//                    .show()
//
//            override fun onCancel() =
//                Toast.makeText(this@FacebookActivity, getString(R.string.facebook_share_cancel), Toast.LENGTH_SHORT)
//                    .show()
//
//            override fun onError(error: FacebookException) =
//                Toast.makeText(
//                    this@FacebookActivity,
//                    getString(R.string.facebook_share_error) + error.localizedMessage,
//                    Toast.LENGTH_LONG
//                ).show()
//        })
//        val sharePhoto = SharePhoto.Builder()
//            .setBitmap(reportImage)
//            .build()
//        if (ShareDialog.canShow(SharePhotoContent::class.java)) {
//            var sharePhotoContent = SharePhotoContent.Builder()
//                .addPhoto(sharePhoto)
//                .build()
//            shareDialog.show(sharePhotoContent)
//        } else {
//            AlertDialog.Builder(this)
//                .setMessage(getString(R.string.facebook_share_impossible))
//                .setPositiveButton(R.string.export_to_file) { _, _ ->
//                    val result =
//                        ExternalStorage().saveImage(model.route.name, reportImage, applicationContext.contentResolver)
//                    if (result.success) {
//                        var s = getString(R.string.image_saved)
//                        if (result.fileName != null) {
//                            s += "under the name" + result.fileName
//                        }
//                        s += "."
//                        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(this, getString(R.string.save_failed), Toast.LENGTH_LONG).show()
//                    }
//                }
//                .setNegativeButton(R.string.cancel) { _, _ -> Log.d(TAG, "canceled") }
//                .show()
//        }
//    }
//
//    override fun getTreasureProgress(): TreasuresProgress? = null
//}