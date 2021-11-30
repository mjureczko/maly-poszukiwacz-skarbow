package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_treasures_editor.*
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


private const val ROUTE_NAME_DIALOG = "RouteNameDialog"

class TreasuresEditorActivity : ActivityWithBackButton(), RouteNameDialog.Callback, TreasurePhotoMaker {

    companion object {
        private val xmlHelper = XmlHelper()
        const val REQUEST_PHOTO = 2
        const val TMP_PICTURE_FILE = "/tmp.jpg"
        private const val SELECTED_LIST = "pl.marianjureczko.poszukiwacz.activity.list_select_to_edit";

        fun intent(packageContext: Context) = Intent(packageContext, TreasuresEditorActivity::class.java)

        fun intent(packageContext: Context, route: Route) =
            Intent(packageContext, TreasuresEditorActivity::class.java).apply {
                putExtra(SELECTED_LIST, xmlHelper.writeToString(route))
            }
    }

    private val TAG = javaClass.simpleName
    private val SETUP_DIALOG_SHOWN_KEY: String = "SETUP_DIALOG_SHOWN"
    private val ROUTE_KEY: String = "ROUTE"

    private val storageHelper = StorageHelper(this)
    private val permissionsManager = PermissionsManager(this)
    lateinit var treasuresRecyclerView: RecyclerView
    lateinit var treasureAdapter: TreasureAdapter

    private val model: TreasuresEditorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addIconToActionBar(supportActionBar)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_treasures_editor)
        permissionsManager.requestMediaPermissions()

        treasuresRecyclerView = findViewById(R.id.treasures)
        treasuresRecyclerView.layoutManager = LinearLayoutManager(this)
        val existingList = intent.getStringExtra(SELECTED_LIST)
        if (isInEditExistingRouteMode(existingList)) {
            setupTreasureView(xmlHelper.loadFromString(existingList))
        } else {
            savedInstanceState?.getString(ROUTE_KEY)?.let { setupTreasureView(xmlHelper.loadFromString(it)) }
            if (isInCreateRouteModeAndDidNotAskForNameYet(savedInstanceState)) {
                showRouteNameDialog()
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

    override fun onNameEntered(name: String) {
        val route = Route(name, ArrayList())
        if (storageHelper.routeAlreadyExists(route)) {
            AlertDialog.Builder(this)
                .setMessage(R.string.overwritting_route)
                .setPositiveButton(R.string.no) { _, _ -> showRouteNameDialog() }
                .setNegativeButton(R.string.yes) { _, _ ->
                    storageHelper.removeRouteByName(name)
                    setupTreasureView(route)
                }
                .show()
        } else {
            setupTreasureView(route)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "########> onActivityResult")
        if (requestCode == REQUEST_PHOTO) {
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(applicationContext, R.string.photo_saving, Toast.LENGTH_SHORT).show()
                val photoTempFile = getPhotoTempFile()
                val photoHelper = PhotoHelper(storageHelper)
                if (model.treasureNeedingPhoto != null && photoHelper.rescaleImageAndSaveInTreasure(photoTempFile, model.treasureNeedingPhoto!!)) {
                    Toast.makeText(applicationContext, R.string.photo_saved, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, R.string.photo_failed, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, R.string.photo_failed, Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun doPhotoForTreasure(treasure: TreasureDescription) {
        model.treasureNeedingPhoto = treasure
        val photoUri = createPhotoUri()

        val captureImage = capturePhotoIntent()
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        for (cameraActivity in cameraActivities) {
            grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(captureImage, REQUEST_PHOTO)
    }

    private fun createPhotoUri(): Uri {
        val photoFile = getPhotoTempFile()
        if (!photoFile.exists()) {
            photoFile.createNewFile()
        }
        return FileProvider.getUriForFile(this, "pl.marianjureczko.poszukiwacz.fileprovider", photoFile)
    }

    private fun getPhotoTempFile() = File(storageHelper.pathToRoutesDir() + TMP_PICTURE_FILE)

    private fun capturePhotoIntent() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun setupTreasureView(route: Route) {
        model.route = route
        treasureAdapter = TreasureAdapter(this, route, permissionsManager, storageHelper, this)
        treasuresRecyclerView.adapter = treasureAdapter
        supportActionBar?.title = "${App.getResources().getString(R.string.route)} ${route.name}"
    }

    private fun isInCreateRouteModeAndDidNotAskForNameYet(savedInstanceState: Bundle?): Boolean =
        savedInstanceState?.getBoolean(SETUP_DIALOG_SHOWN_KEY) == null

    private fun isInEditExistingRouteMode(existingList: String?) =
        existingList != null

    private fun showRouteNameDialog() =
        RouteNameDialog.newInstance().apply {
            show(this@TreasuresEditorActivity.supportFragmentManager, ROUTE_NAME_DIALOG)
        }
}

