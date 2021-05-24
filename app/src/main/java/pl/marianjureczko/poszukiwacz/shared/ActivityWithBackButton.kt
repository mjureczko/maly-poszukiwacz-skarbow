package pl.marianjureczko.poszukiwacz.shared

import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import pl.marianjureczko.poszukiwacz.R

abstract class ActivityWithBackButton : AppCompatActivity() {
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_back, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.back) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}