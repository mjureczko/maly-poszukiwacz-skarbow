package pl.marianjureczko.poszukiwacz.activity.commemorative

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.facebook.FacebookInputData
import pl.marianjureczko.poszukiwacz.databinding.ActivityCommemorativeBinding
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

class CommemorativeActivity : ActivityWithAdsAndBackButton() {

    companion object {
        const val INPUT = "pl.marianjureczko.poszukiwacz.activity.commemorative"
    }

    private lateinit var binding: ActivityCommemorativeBinding
    private val model: CommemorativeViewModel by viewModels()
    private val storageHelper = StorageHelper(this)
    private val photoHelper = PhotoHelper(this, storageHelper)
    private val doPhotoLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
        if (result) {
            photoHelper.moveCommemorativePhotoToPermanentLocation(target = model.commemorativePhotoAbsolutePath)
            reloadImage(model.commemorativePhotoUri())
            Toast.makeText(this, R.string.photo_replaced, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.photo_not_replaced, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommemorativeBinding.inflate(layoutInflater)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_commemorative)

        val input = intent.getSerializableExtra(INPUT) as CommemorativeInputData
        model.initialize(input.progress, input.photoAbsolutePath)

        binding.photoImg.setImageURI(model.commemorativePhotoUri())
        binding.rotatePhotoBtn.setOnClickListener {
            rotatePhoto(input.photoAbsolutePath, model.commemorativePhotoUri())
        }
        binding.doPhotoBtn.setOnClickListener {
            doPhotoLauncher.launch(photoHelper.createCommemorativePhotoTempUri())
        }
        setContentView(binding.root)

        setUpAds(binding.adView)
    }

    override fun getTreasureProgress(): TreasuresProgress? = model.progress

    private fun rotatePhoto(photoFullPath: String, uri: Uri) {
        lifecycleScope.launch {
            PhotoHelper.rotateGraphicClockwise(photoFullPath) {
                reloadImage(uri)
            }
        }
    }

    private fun reloadImage(uri: Uri) {
        binding.photoImg.setImageURI(null)
        binding.photoImg.setImageURI(uri)
    }
}