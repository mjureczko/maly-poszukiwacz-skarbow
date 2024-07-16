//package pl.marianjureczko.poszukiwacz.activity.treasureseditor
//
//import android.app.AlertDialog
//import android.app.Dialog
//import android.os.Bundle
//import android.text.InputType
//import android.widget.EditText
//import androidx.fragment.app.DialogFragment
//import pl.marianjureczko.poszukiwacz.R
//
//class RouteNameDialog : DialogFragment() {
//    interface Callback {
//        fun onNameEntered(name: String)
//    }
//
//    private val TAG = javaClass.simpleName
//
//    companion object {
//        fun newInstance(): RouteNameDialog {
//            return RouteNameDialog()
//        }
//    }
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
//        builder.setTitle(R.string.route_name_prompt)
//        val input = EditText(activity)
//        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
//        builder.setView(input)
//        builder.setPositiveButton(R.string.ok) { _, _ ->
//            val name = input.text.toString()
//            activity?.let { activity ->
//                (activity as Callback).onNameEntered(name)
//            }
//        }
//        return builder.create()
//    }
//}