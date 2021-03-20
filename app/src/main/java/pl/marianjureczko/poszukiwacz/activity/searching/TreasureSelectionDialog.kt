package pl.marianjureczko.poszukiwacz.activity.searching

import android.app.Activity
import android.app.AlertDialog
import android.location.Location
import android.widget.ArrayAdapter
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route

interface TreasureLocationStorage {
    fun selectTreasure(which: Int)
    fun getCurrentLocation(): Location?
}

class TreasureSelectionDialog(
    private val activity: Activity,
    private val treasureLocationStorage: TreasureLocationStorage
) {

    private val locationCalculator = LocationCalculator()

    fun show(treasures: Route) {
        val arrayAdapter = prepareArrayAdapterWithTreasuresDescriptions(treasures)
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.select_treasure_dialog_title)
        builder.setAdapter(arrayAdapter) { dialog, which ->
            treasureLocationStorage.selectTreasure(which)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun prepareArrayAdapterWithTreasuresDescriptions(treasures: Route): ArrayAdapter<String> {
        val message = App.getResources().getString(R.string.steps_to_treasure)
        val arrayAdapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice)
        treasures.treasures.forEach { treasure ->
            if (treasureLocationStorage.getCurrentLocation() != null) {
                val distance = locationCalculator.distanceInSteps(treasure, treasureLocationStorage.getCurrentLocation()!!)
                arrayAdapter.add("[${treasure.id}] $distance $message")
            } else {
                arrayAdapter.add(treasure.prettyName())
            }
        }
        return arrayAdapter
    }
}