//package pl.marianjureczko.poszukiwacz.activity.result
//
//import android.app.Activity
//import android.content.pm.ActivityInfo
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.activity.viewModels
//import androidx.core.view.isVisible
//import pl.marianjureczko.poszukiwacz.R
//import pl.marianjureczko.poszukiwacz.databinding.ActivityResultBinding
//import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
//import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
//import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
//import pl.marianjureczko.poszukiwacz.shared.StorageHelper
//
//class ResultActivity : ActivityWithAdsAndBackButton() {
//
//    companion object {
//        const val RESULT_IN = "pl.marianjureczko.poszukiwacz.activity.result_in"
//        const val RESULT_OUT = "pl.marianjureczko.poszukiwacz.activity.result_out"
//    }
//
//    private val model: ResultActivityViewModel by viewModels()
//    private lateinit var binding: ActivityResultBinding
//    private val storageHelper = StorageHelper(this)
//    private val photoHelper = PhotoHelper(this, storageHelper)
//
//    private val showCommemorativeLauncher: ActivityResultLauncher<pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeInputData> =
//        registerForActivityResult(pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeContract()) {}
//
//    private val doPhotoLauncher: ActivityResultLauncher<Uri> =
//        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
//            if (result) {
//                model.addCommemorativePhoto(storageHelper, photoHelper.moveCommemorativePhotoToPermanentLocation())
//                configureDoPhotoButton()
//                Toast.makeText(this, R.string.photo_saved, Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(this, R.string.photo_not_replaced, Toast.LENGTH_SHORT).show()
//            }
//        }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_result)
//
//        binding = ActivityResultBinding.inflate(layoutInflater)
//
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//        val input = intent.getSerializableExtra(RESULT_IN) as ResultActivityInput
//        model.initialize(this, storageHelper, input.treasure, input.progress, input.treasureDescription)
//
//        configureView()
//        setContentView(binding.root)
//        setUpAds(binding.adView)
//    }
//
//    private fun configureView() {
//        if (model.isError()) {
//            binding.resultDescription.text = model.errorMsg
//            binding.resultImg.visibility = View.GONE
//        } else {
//            binding.resultDescription.text = model.treasure!!.quantity.toString()
//            binding.resultImg.setImageResource(model.treasure!!.type.image())
//        }
//
//        if (model.canMakeCommemorativePhoto()) {
//            configureDoPhotoButton()
//        } else {
//            binding.buttonsLayout.isVisible = false
//        }
//    }
//
//    private fun configureDoPhotoButton() {
//        binding.buttonsLayout.isVisible = true
//        if (model.currentTreasureHasCommemorativePhoto()) {
//            binding.doPhoto.setImageResource(R.drawable.camera_show_photo)
//            binding.doPhoto.setOnClickListener {
//                model.commemorativeInputData()?.let { showCommemorativeLauncher.launch(it) }
//            }
//        } else {
//            binding.doPhoto.setImageResource(R.drawable.camera_do_photo)
//            binding.doPhoto.setOnClickListener {
//                doPhotoLauncher.launch(photoHelper.getCommemorativePhotoTempUri())
//            }
//        }
//    }
//
//    override fun getTreasureProgress(): TreasuresProgress? {
//        return null
//    }
//
//    override fun onBackPressed() {
//        setResult(Activity.RESULT_OK, model.activityResult())
//        super.onBackPressed()
//    }
//}