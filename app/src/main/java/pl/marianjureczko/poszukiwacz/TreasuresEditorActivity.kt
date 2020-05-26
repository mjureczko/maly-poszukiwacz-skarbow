package pl.marianjureczko.poszukiwacz

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class TreasuresEditorActivity : AppCompatActivity() {

    companion object {
        var treasuresList = TreasuresList("Nienazwana", ArrayList())
    }

    val storageHelper = StorageHelper(this)
    var treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
    lateinit var list: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treasures_editor)
        setupTreasuresListUsingDialog()

        val addTreasureButton = findViewById<Button>(R.id.add_treasure)
        val lon = findViewById<TextView>(R.id.editorLongValue)
        val lat = findViewById<TextView>(R.id.editorLatValue)
        list = findViewById(R.id.treasures)
        list.adapter = treasuresAdapter

        addTreasureButton.setOnClickListener {
            val treasure = TreasureDescription(
                longitude = lon.text.toString().toDouble(),
                latitude = lat.text.toString().toDouble()
            )
            treasuresList.tresures.add(treasure)
            treasuresAdapter.notifyDataSetChanged()
            storageHelper.save(treasuresList)
        }
    }

    private fun setupTreasuresListUsingDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Podaj nazwę listy skarbów:")
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        builder.setView(input)
        val listName = findViewById<TextView>(R.id.treasures_list_name)
        builder.setPositiveButton("OK") { dialog, which ->
            val name = input.text.toString()
            treasuresList = TreasuresList(name, ArrayList())
            treasuresAdapter = TreasuresAdapter(treasuresList, this, storageHelper)
            list.adapter = treasuresAdapter
            listName.text = name
        }
        builder.show()
    }
}
