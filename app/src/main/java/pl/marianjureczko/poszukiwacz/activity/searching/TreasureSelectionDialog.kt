package pl.marianjureczko.poszukiwacz.activity.searching

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import androidx.fragment.app.DialogFragment
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route

interface TreasureLocationStorage {
    fun selectTreasure(which: Int)
    fun getCurrentLocation(): Location?
}

private const val ROUTE = "route"

class TreasureSelectionDialog : DialogFragment() {
    private val TAG = javaClass.simpleName
    private val locationCalculator = LocationCalculator()
    var treasureLocationStorage: TreasureLocationStorage? = null

    companion object {
        fun newInstance(route: Route): TreasureSelectionDialog {
            val args = Bundle().apply {
                putSerializable(ROUTE, route)
            }

            return TreasureSelectionDialog().apply {
                arguments = args
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(R.string.select_treasure_dialog_title)
        arguments?.getSerializable(ROUTE).let { route ->
            route as Route
            val arrayAdapter = prepareArrayAdapterWithTreasuresDescriptions(route)
            builder.setAdapter(arrayAdapter) { dialog, which ->
                treasureLocationStorage?.selectTreasure(which)
                dialog.dismiss()
            }
        }
        return builder.create()
    }

    private fun prepareArrayAdapterWithTreasuresDescriptions(treasures: Route): ArrayAdapter<String> {
        val arrayAdapter: ArrayAdapter<String> =
            object : ArrayAdapter<String>(activity?.baseContext!!, android.R.layout.select_dialog_singlechoice) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val textView = super.getView(position, convertView, parent) as CheckedTextView
                    val black = Color.parseColor("#000000")
                    textView.setTextColor(black)
                    textView.setCheckMarkDrawable(R.drawable.circle)
                    return textView
                }
            }

        treasures.treasures.forEach { treasure ->
            if (treasureLocationStorage?.getCurrentLocation() != null) {
                val distance: Int = locationCalculator.distanceInSteps(treasure, treasureLocationStorage?.getCurrentLocation()!!)
                arrayAdapter.add(activity?.getString(R.string.steps_to_treasure, treasure.id, distance))
            } else {
                arrayAdapter.add(treasure.prettyName())
            }
        }

        return arrayAdapter
    }
}
