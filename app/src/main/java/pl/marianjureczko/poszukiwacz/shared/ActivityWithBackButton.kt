package pl.marianjureczko.poszukiwacz.shared

import android.os.Bundle
import android.os.PersistableBundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pl.marianjureczko.poszukiwacz.R

abstract class ActivityWithBackButton : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setIcon(R.drawable.chest_very_small)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}