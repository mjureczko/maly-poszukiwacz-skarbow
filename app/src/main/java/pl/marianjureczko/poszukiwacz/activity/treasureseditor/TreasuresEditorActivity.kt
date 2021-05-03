package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_treasures_editor.*
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.LocationRequester
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper
import pl.marianjureczko.poszukiwacz.shared.addIconToActionBar

interface RecordingPermission {
    fun granted(): Boolean
}

private const val ROUTE_NAME_DIALOG = "RouteNameDialog"

class TreasuresEditorActivity : AppCompatActivity(), RecordingPermission, RouteNameDialog.Callback {

    companion object {
        private val xmlHelper = XmlHelper()
        const val REQUEST_PHOTO = 2
        private const val SELECTED_LIST = "pl.marianjureczko.poszukiwacz.activity.list_select_to_edit";

        fun intent(packageContext: Context) = Intent(packageContext, TreasuresEditorActivity::class.java)

        fun intent(packageContext: Context, route: Route) =
            Intent(packageContext, TreasuresEditorActivity::class.java).apply {
                putExtra(SELECTED_LIST, xmlHelper.writeToString(route))
            }
    }

    private val TAG = javaClass.simpleName
    private val REQUEST_RECORD_AUDIO_PERMISSION = 200
    private val SETUP_DIALOG_SHOWN_KEY: String = "SETUP_DIALOG_SHOWN"
    private val ROUTE_KEY: String = "ROUTE"
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)
    private val storageHelper = StorageHelper(this)
    lateinit var treasuresRecyclerView: RecyclerView
    lateinit var treasureAdapter: TreasureAdapter

    private val model: TreasuresEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_treasures_editor)
        requestRecordingPermission()

        treasuresRecyclerView = findViewById(R.id.treasures)
        treasuresRecyclerView.layoutManager = LinearLayoutManager(this)
        val existingList = intent.getStringExtra(SELECTED_LIST)
        if (isInEditExistingRouteMode(existingList)) {
            setupTreasureView(xmlHelper.loadFromString(existingList))
            //TODO: route and treasuresAdapter are changed together
        } else {
            savedInstanceState?.getString(ROUTE_KEY)?.let { setupTreasureView(xmlHelper.loadFromString(it)) }
            if (isInCreateRouteModeAndDidNotAskForNameYet(savedInstanceState)) {
                RouteNameDialog.newInstance().apply {
                    show(this@TreasuresEditorActivity.supportFragmentManager, ROUTE_NAME_DIALOG)
                }
                savedInstanceState?.putBoolean(SETUP_DIALOG_SHOWN_KEY, true)
            }
        }

        add_treasure.setOnClickListener {
            val treasure = TreasureDescription(
                id = model.route.nextId(),
                latitude = editorLatValue.text.toString().toDouble(),
                longitude = editorLongValue.text.toString().toDouble()
            )
            model.route.treasures.add(treasure)
            treasureAdapter.notifyDataSetChanged()
            storageHelper.save(model.route)
        }

        val locationListener = TextViewBasedLocationListener(editorLatValue, editorLongValue)
        //TODO: copied from SearchingActivity
        val handler = Handler()
        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = LocationRequester(this, locationListener, handler, location)
        handler.post(presenter)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d(TAG, "########> onSaveInstanceState")
        if (model.route != Route.nullObject()) {
            outState.putString(ROUTE_KEY, xmlHelper.writeToString(model.route))
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(code: Int, perms: Array<String>, results: IntArray) {
        super.onRequestPermissionsResult(code, perms, results)
        model.permissionToRecordAccepted = if (code == REQUEST_RECORD_AUDIO_PERMISSION) {
            results[0] == PackageManager.PERMISSION_GRANTED
        } else {
            false
        }
    }

    override fun granted(): Boolean = model.permissionToRecordAccepted

    override fun onNameEntered(name: String) {
        setupTreasureView(Route(name, ArrayList()))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "########> onActivityResult")
        if (requestCode == REQUEST_PHOTO) {
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(applicationContext, R.string.photo_saved, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, R.string.photo_failed, Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun setupTreasureView(route: Route) {
        model.route = route
        treasureAdapter = TreasureAdapter(this, route, this, storageHelper)
        treasuresRecyclerView.adapter = treasureAdapter
        supportActionBar?.title = "${App.getResources().getString(R.string.route)} ${route.name}"
    }

    private fun requestRecordingPermission() =
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION)

    private fun isInCreateRouteModeAndDidNotAskForNameYet(savedInstanceState: Bundle?): Boolean =
        savedInstanceState?.getBoolean(SETUP_DIALOG_SHOWN_KEY) == null

    private fun isInEditExistingRouteMode(existingList: String?) =
        existingList != null

}

