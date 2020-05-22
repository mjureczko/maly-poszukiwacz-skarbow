package pl.marianjureczko.poszukiwacz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("########> onCreate ${System.currentTimeMillis() % 100_000}")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        dummy_button.setOnTouchListener(mDelayHideTouchListener)
        val treasures = createAndLoadTreasures()

        showTreasuresLists(treasures)

        val newListButton = findViewById<Button>(R.id.new_list_button)
        newListButton.setOnClickListener {
            val intent = Intent(this, TreasuresEditorActivity::class.java).apply {
//                putExtra(EXTRA_MESSAGE, message)
            }
            startActivity(intent)
        }
    }

    private fun showTreasuresLists(treasures: MutableList<TreasuresList>) {
        val treasuresList = findViewById<ListView>(R.id.treasures_list)
        val adapter = TreasuresListsAdapter(treasures, this)
        treasuresList.adapter = adapter
    }

    private fun createAndLoadTreasures(): MutableList<TreasuresList> {
        val storageHelper = StorageHelper(this)
        createSomeTreasures(storageHelper)
        val treasures = storageHelper.loadAll()
        println("########> Tresures ${treasures.size}")
        return treasures
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        println("########> onSaveInstanceState ${System.currentTimeMillis() % 100_000}")
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)

    }

    private fun createSomeTreasures(storageHelper: StorageHelper) {
        storageHelper.save(
            TreasuresList(
                "test1", ArrayList(listOf(
                    TreasureDescription(1.1, 1.2),
                    TreasureDescription(2.1, 2.2)
                ))
            )
        )
        storageHelper.save(
            TreasuresList(
                "test2", ArrayList(listOf(
                    TreasureDescription(1.1, 3.2),
                    TreasureDescription(2.1, 3.2)
                ))
            )
        )
    }

}
