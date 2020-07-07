package pl.marianjureczko.poszukiwacz.dialog

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.widget.ArrayAdapter
import pl.marianjureczko.poszukiwacz.TreasuresList
import pl.marianjureczko.poszukiwacz.activity.TreasureLocationView


class TreasureSelectionDialog(val activity: Activity, val treasureLocationView : TreasureLocationView) {

    fun show(treasures: TreasuresList) {
        val arrayAdapter = ArrayAdapter<String>(activity, R.layout.select_dialog_singlechoice)
        treasures.treasures.forEach {
            arrayAdapter.add(it.prettyName())
        }

        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Wybierz skarb")
        builder.setAdapter(arrayAdapter) { dialog, which ->
            treasureLocationView.showTreasureLocation(which)
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}