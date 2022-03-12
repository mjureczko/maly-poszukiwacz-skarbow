package pl.marianjureczko.poszukiwacz.activity.photo

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityPhotoBinding
import pl.marianjureczko.poszukiwacz.shared.ActivityWithBackButton

class PhotoActivity : ActivityWithBackButton() {
    companion object {
        private const val PHOTO = "pl.marianjureczko.poszukiwacz.activity.list_select_to_edit";

        fun intent(packageContext: Context, photo: String) =
            Intent(packageContext, PhotoActivity::class.java).apply {
                putExtra(PHOTO, photo)
            }
    }

    private lateinit var binding: ActivityPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_photo)
        val photo = intent.getStringExtra(PHOTO)
        val uri = Uri.parse(photo)
        binding.photoImg.setImageURI(uri)
        setContentView(binding.root)
    }
}