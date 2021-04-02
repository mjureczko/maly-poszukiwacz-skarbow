package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Chronometer
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import pl.marianjureczko.poszukiwacz.R

private const val FILENAME = "filename"
private const val CLOSED_AT = "closed_at"

class RecordingDialog : DialogFragment() {
    private val TAG = javaClass.simpleName
    private var recorder: MediaRecorder? = null
    private var chronometer: Chronometer? = null
    private var dialog: AlertDialog? = null

    companion object {
        fun newInstance(fileName: String): RecordingDialog {
            val args = Bundle().apply {
                putSerializable(FILENAME, fileName)
            }

            return RecordingDialog().apply {
                arguments = args
            }
        }
    }

    override fun onStop() {
        super.onStop()
        stopRecording()
        if (chronometer != null) {
            arguments?.putString(CLOSED_AT, chronometer!!.text as String)
        }
    }

    override fun onResume() {
        super.onResume()
        val closedAt = arguments?.getSerializable(CLOSED_AT) as String?
        if (closedAt != null) {
            chronometer?.stop()
            dialog?.getButton(DialogInterface.BUTTON_NEUTRAL)?.setText(R.string.close)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fileName = arguments?.getSerializable(FILENAME) as String
        val closedAt = arguments?.getSerializable(CLOSED_AT) as String?
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        chronometer = createChronometer()
        if (closedAt != null) {
            builder.setNeutralButton(R.string.close) { _, _ -> dismiss() }
            chronometer?.text = closedAt
        } else {
            builder.setNeutralButton(R.string.stop_recording) { _, _ -> dismiss() }
            chronometer?.setOnChronometerTickListener {
                arguments?.putString(CLOSED_AT, it.text as String)
            }
            chronometer?.start()
            startRecording(fileName)
        }
        builder.setView(chronometer)
        dialog = builder.create()
        return dialog!!
    }

    private fun createChronometer(): Chronometer {
        val chronometer = Chronometer(activity)
        chronometer.textSize = 50.0f
        chronometer.gravity = Gravity.CENTER
        return chronometer
    }

    private fun startRecording(fileName: String) {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileName)
            try {
                prepare()
                start()
            } catch (e: Exception) {
                Log.e(TAG, "Audio recording failed ${e.message}", e)
                Toast.makeText(activity, R.string.tip_recording_failed, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            Toast.makeText(activity, R.string.tip_recorded, Toast.LENGTH_SHORT).show()
            release()
        }
        recorder = null
    }
}