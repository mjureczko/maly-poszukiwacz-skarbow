package pl.marianjureczko.poszukiwacz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
        const val SELECTED_LIST = "SELECTED_LIST"
    }

    private lateinit var storageHelper : StorageHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        println("########> onCreate ${System.currentTimeMillis() % 100_000}")
        super.onCreate(savedInstanceState)
        storageHelper = StorageHelper(this)

        setContentView(R.layout.activity_main)

        val treasures = storageHelper.loadAll()
        showTreasuresLists(treasures)

        val newListButton = findViewById<Button>(R.id.new_list_button)
        newListButton.setOnClickListener {
            val intent = Intent(this, TreasuresEditorActivity::class.java).apply {}
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        println("########> onResume ${System.currentTimeMillis() % 100_000}")
        val treasures = storageHelper.loadAll()
        showTreasuresLists(treasures)
    }

    private fun showTreasuresLists(treasures: MutableList<TreasuresList>) {
        val treasuresList = findViewById<ListView>(R.id.treasures_lists)
        val adapter = TreasuresListsAdapter(treasures, this, storageHelper)
        treasuresList.adapter = adapter
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        println("########> onSaveInstanceState ${System.currentTimeMillis() % 100_000}")
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)

    }
}
