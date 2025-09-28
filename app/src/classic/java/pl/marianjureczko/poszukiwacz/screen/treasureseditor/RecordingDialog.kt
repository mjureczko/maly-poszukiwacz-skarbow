package pl.marianjureczko.poszukiwacz.screen.treasureseditor

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.Gravity
import android.widget.Chronometer
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.AbstractDialog
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes
import pl.marianjureczko.poszukiwacz.ui.theme.buttonColors

const val STOP_RECORDING_BUTTON = "stop recording button"
private const val FILENAME = "filename"
private const val CLOSED_AT = "closed_at"

@Composable
fun RecordingDialog(
    fileName: String,
    onDismiss: (Long?) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var isRecording = remember { mutableStateOf(true) }
    var startedTime = remember { mutableStateOf(0L) }
    var closedAt = remember { mutableStateOf<Long?>(null) }
    val recorder = remember { MediaRecorder() }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP) {
                stopRecording(recorder, context)
                onDismiss(closedAt.value)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            stopRecording(recorder, context)
        }
    }

    LaunchedEffect(isRecording.value) {
        if (isRecording.value) {
            startRecording(recorder, fileName, context)
            startedTime.value = SystemClock.elapsedRealtime()
            while (isRecording.value) {
                delay(1000)
                closedAt.value = SystemClock.elapsedRealtime() - startedTime.value
            }
        }
    }

    AbstractDialog(
        visible = true,
        hideIt = {
            isRecording.value = false
            stopRecording(recorder, context)
            onDismiss(closedAt.value)
        },
        title = null,
        titleString = formatTime(closedAt.value),
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    shape = Shapes.large,
                    colors = buttonColors(),
                    onClick = {
                        isRecording.value = false
                        stopRecording(recorder, context)
                        onDismiss(closedAt.value)
                    },
                    modifier = Modifier.semantics { contentDescription = STOP_RECORDING_BUTTON }
                ) {
                    Text(text = context.getString(R.string.stop_recording))
                }
            }
        },
    )
}

private fun startRecording(recorder: MediaRecorder, fileName: String, context: android.content.Context) {
    recorder.apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        setOutputFile(fileName)
        try {
            prepare()
            start()
        } catch (e: Exception) {
            Log.e("RecordingDialog", "Audio recording failed: ${e.message}", e)
            Toast.makeText(context, context.getString(R.string.tip_recording_failed), Toast.LENGTH_SHORT).show()
        }
    }
}

private fun stopRecording(recorder: MediaRecorder, context: android.content.Context) {
    try {
        recorder.apply {
            stop()
            release()
            Toast.makeText(context, context.getString(R.string.tip_recorded), Toast.LENGTH_SHORT).show()
        }
    } catch (e: Exception) {
        Log.e("RecordingDialog", "Failed to stop recording: ${e.message}", e)
    }
}

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