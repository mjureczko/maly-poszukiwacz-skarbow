package pl.marianjureczko.poszukiwacz.dialog

import android.app.Activity
import android.app.AlertDialog
import android.media.MediaRecorder
import android.util.Log
import android.view.Gravity
import android.widget.Chronometer
import android.widget.Toast
import pl.marianjureczko.poszukiwacz.R
import java.io.IOException

class RecordingDialog(val activity: Activity, val fileName: String) {
    private val TAG = javaClass.simpleName
    private var recorder: MediaRecorder? = null

    fun show(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        configureChronometerView(builder)
        builder.setNeutralButton(R.string.stop_recording) { dialog, _ -> dialog.dismiss() }
        builder.setOnDismissListener { _ -> stopRecording() }
        startRecording()
        val dialog = builder.create()
        dialog.show()
        return dialog
    }

    private fun configureChronometerView(builder: AlertDialog.Builder) {
        val chronometer = Chronometer(activity)
        chronometer.textSize = 50.0f
        chronometer.gravity = Gravity.CENTER
        chronometer.start()
        builder.setView(chronometer)
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(fileName)
            try {
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(TAG, "Audio recording failed ${e.message}")
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