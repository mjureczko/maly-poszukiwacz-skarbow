package pl.marianjureczko.poszukiwacz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class TreasuresEditorActivity : AppCompatActivity() {

    val storageHelper = StorageHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasures_editor)

    }
}
