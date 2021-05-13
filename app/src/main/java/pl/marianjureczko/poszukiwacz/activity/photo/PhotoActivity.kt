package pl.marianjureczko.poszukiwacz.activity.photo

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_photo.*
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar

class PhotoActivity : AppCompatActivity() {
    companion object {
        private const val PHOTO = "pl.marianjureczko.poszukiwacz.activity.list_select_to_edit";

        fun intent(packageContext: Context, photo: String) =
            Intent(packageContext, PhotoActivity::class.java).apply {
                putExtra(PHOTO, photo)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo)
        addIconToActionBar(supportActionBar)
        val photo = intent.getStringExtra(PHOTO)
        val uri = Uri.parse(photo)
        photoImg.setImageURI(uri)
        photoImg.rotation = 270f
    }
}