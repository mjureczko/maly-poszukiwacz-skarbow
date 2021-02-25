package pl.marianjureczko.poszukiwacz.activity.searching

import android.app.Activity
import android.app.AlertDialog
import android.widget.ArrayAdapter
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.Route

interface TreasureLocationView {
    fun showTreasureLocation(which: Int)
}

class TreasureSelectionDialog(val activity: Activity, val treasureLocationView : TreasureLocationView) {

    fun show(treasures: Route) {
        val arrayAdapter = ArrayAdapter<String>(activity, android.R.layout.select_dialog_singlechoice)
        treasures.treasures.forEach {
            arrayAdapter.add(it.prettyName())
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.select_treasure_dialog_title)
        builder.setAdapter(arrayAdapter) { dialog, which ->
            treasureLocationView.showTreasureLocation(which)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}