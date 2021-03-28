package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.AlertDialog
import android.app.Dialog
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

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val fileName = arguments?.getSerializable(FILENAME) as String
        val closedAt = arguments?.getSerializable(CLOSED_AT) as String?
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        if (closedAt != null) {
            builder.setNeutralButton(R.string.close) { _, _ ->
                dismiss()
            }
            configureStaticChronometerView(builder, closedAt)
        } else {
            builder.setNeutralButton(R.string.stop_recording) { _, _ ->
                dismiss()
            }
            configureChronometerView(builder)
            startRecording(fileName)
        }
        return builder.create()
    }

    private fun configureChronometerView(builder: AlertDialog.Builder) {
        val chronometer = Chronometer(activity)
        formatChronometer(chronometer)
        chronometer.start()
        chronometer.setOnChronometerTickListener {
            arguments?.putString(CLOSED_AT, it.text as String)
        }
        builder.setView(chronometer)
    }

    private fun configureStaticChronometerView(builder: AlertDialog.Builder, chronometerText: String) {
        val chronometer = Chronometer(activity)
        chronometer.text = chronometerText
        formatChronometer(chronometer)
        builder.setView(chronometer)
    }

    private fun formatChronometer(chronometer: Chronometer) {
        chronometer.textSize = 50.0f
        chronometer.gravity = Gravity.CENTER
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