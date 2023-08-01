package pl.marianjureczko.poszukiwacz.shared

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress

class SelectTreasureProgressDialog(private val progresses: List<TreasuresProgress>) : DialogFragment() {
    interface Callback {
        fun onTreasureProgressSelected(routeName: String)
    }

    companion object {
        fun newInstance(progresses: List<TreasuresProgress>): SelectTreasureProgressDialog {
            return SelectTreasureProgressDialog(progresses)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle(R.string.select_treasure_progress_to_share)
                .setItems(progresses.map { it.routeName }.toTypedArray()) { _, which ->
                    activity?.let { activity ->
                        (activity as Callback).onTreasureProgressSelected(progresses[which].routeName)
                    }
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

}