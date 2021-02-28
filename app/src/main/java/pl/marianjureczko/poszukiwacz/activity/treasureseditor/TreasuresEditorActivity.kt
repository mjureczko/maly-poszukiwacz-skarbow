package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.marianjureczko.poszukiwacz.*

interface RecordingPermission {
    fun granted(): Boolean
}

class TreasuresEditorActivity() : AppCompatActivity(), RecordingPermission {

    companion object {
        private var route = Route("???", ArrayList())
        private val xmlHelper = XmlHelper()
        private const val SELECTED_LIST = "pl.marianjureczko.poszukiwacz.activity.list_select_to_edit";

        fun intent(packageContext: Context) = Intent(packageContext, TreasuresEditorActivity::class.java)

        fun intent(packageContext: Context, route: Route) =
            Intent(packageContext, TreasuresEditorActivity::class.java).apply {
                putExtra(SELECTED_LIST, xmlHelper.writeToString(route))
            }
    }

    private val TAG = javaClass.simpleName
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private val SHOW_SETUP_DIALOG: String = "SHOW_SETUP_DIALOG"
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val storageHelper = StorageHelper(this)
    lateinit var treasuresRecyclerView: RecyclerView
    lateinit var treasureAdapter: TreasureAdapter
    private var showSetupDialog: Boolean = false
    private var setupDialog: AlertDialog? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_treasures_editor)
        requestRecordingPermission()

        val addTreasureButton = findViewById<ImageButton>(R.id.add_treasure)
        val lat = findViewById<TextView>(R.id.editorLatValue)
        val lon = findViewById<TextView>(R.id.editorLongValue)
        treasuresRecyclerView = findViewById(R.id.treasures)
        treasuresRecyclerView.layoutManager = LinearLayoutManager(this)
        val existingList = intent.getStringExtra(SELECTED_LIST)
        if (existingList != null) {
            route = xmlHelper.loadFromString(existingList)
            setupAdapter(route)
            //TODO: route and treasuresAdapter are changed together
        } else {
            showSetupDialog = true
        }

        addTreasureButton.setOnClickListener {
            val treasure = TreasureDescription(
                latitude = lat.text.toString().toDouble(),
                longitude = lon.text.toString().toDouble()
            )
            route.treasures.add(treasure)
            treasureAdapter.notifyDataSetChanged()
            storageHelper.save(route)
        }

        val locationListener = TextViewBasedLocationListener(lat, lon)
        //TODO: copied from SearchingActivity
        val handler = Handler()
        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = LocationPresenter(this, locationListener, handler, location)
        handler.post(presenter)
        restoreState(savedInstanceState)
    }

    private fun setupRouteUsingDialog(): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.route_name_prompt)
        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        builder.setView(input)
        val routeName = findViewById<TextView>(R.id.route_name)
        builder.setPositiveButton(R.string.ok) { _, _ ->
            val name = input.text.toString()
            route = Route(name, ArrayList())
            setupAdapter(route)
            routeName.text = name
        }
        return builder.show()
    }

    private fun setupAdapter(route: Route) {
        treasureAdapter = TreasureAdapter(this, route, this, storageHelper)
        treasuresRecyclerView.adapter = treasureAdapter
    }

    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(code, perms, results)
        permissionToRecordAccepted = if (code == REQUEST_RECORD_AUDIO_PERMISSION) {
            results[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }

    private fun requestRecordingPermission() {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)
    }

    private fun conditionallyShowSetupDialog() {
        if (showSetupDialog) {
            showSetupDialog = try {
                setupDialog = setupRouteUsingDialog()
                false
            } catch (ex: Throwable) {
                true
            }
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        Log.e(TAG, "########> onPostResume ${System.currentTimeMillis() % 100_000}")
        conditionallyShowSetupDialog()
    }

    override fun onDestroy() {
        Log.e(TAG, "########> onDestroy ${System.currentTimeMillis() % 100_000}")
        super.onDestroy()
        setupDialog?.dismiss()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        Log.e(TAG, "########> onRestoreInstanceState ${System.currentTimeMillis() % 100_000}")
        restoreState(savedInstanceState)
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        Log.e(TAG, "######## > onSaveInstanceState ${System.currentTimeMillis() % 100_000}")
        outState.run {
            putBoolean(SHOW_SETUP_DIALOG, showSetupDialog)
        }
        super.onSaveInstanceState(outState)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        savedInstanceState?.getBoolean(SHOW_SETUP_DIALOG)?.let {
            showSetupDialog = it
            conditionallyShowSetupDialog()
        }
    }

    override fun granted(): Boolean = permissionToRecordAccepted
}
