package pl.marianjureczko.poszukiwacz.activity.photo

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityPhotoBinding
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton

class PhotoActivity : ActivityWithAdsAndBackButton() {
    companion object {
        const val INPUT = "pl.marianjureczko.poszukiwacz.activity.photo";
    }

    private lateinit var binding: ActivityPhotoBinding
    private val model: PhotoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_photo)
        val input = intent.getSerializableExtra(INPUT) as PhotoInputData
        val uri = Uri.parse(input.photo)
        binding.photoImg.setImageURI(uri)
        setContentView(binding.root)

        model.initialize(input.progress)

        setUpAds(binding.adView)
    }

    override fun getTreasureProgress(): TreasuresProgress =
        model.progress

}