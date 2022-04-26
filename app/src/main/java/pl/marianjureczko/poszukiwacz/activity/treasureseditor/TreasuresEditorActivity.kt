package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.LinearLayoutManager
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityTreasuresEditorBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip
import pl.marianjureczko.poszukiwacz.shared.LocationRequester
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import java.io.File

private const val ROUTE_NAME_DIALOG = "RouteNameDialog"

class TreasuresEditorActivity : PermissionActivity(), RouteNameDialog.Callback, TreasurePhotoMaker {

    companion object {
        const val REQUEST_PHOTO = 2
        const val TMP_PICTURE_FILE = "/tmp.jpg"
        private const val SELECTED_ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_selected_to_edit";

        fun intent(packageContext: Context) = Intent(packageContext, TreasuresEditorActivity::class.java)

        fun intent(packageContext: Context, routeName: String) =
            Intent(packageContext, TreasuresEditorActivity::class.java).apply {
                putExtra(SELECTED_ROUTE, routeName)
            }
    }

    private val TAG = javaClass.simpleName
    private val storageHelper = StorageHelper(this)
    lateinit var treasureAdapter: TreasureAdapter
    private lateinit var binding: ActivityTreasuresEditorBinding

    private val model: TreasuresEditorViewModel by viewModels()

    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {
        //do nothing
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTreasuresEditorBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        assurePermissionsAreGranted(RequirementsForPhotoAndAudioTip, false)

        binding.treasures.layoutManager = LinearLayoutManager(this)

        restoreState(savedInstanceState)

        binding.addTreasure.setOnClickListener { addTreasureListener() }

        val locationListener = TextViewBasedLocationListener(binding.editorLatValue, binding.editorLongValue)
        val handler = Handler()
        val location = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val presenter = LocationRequester(this, locationListener, handler, location)
        handler.post(presenter)
        setContentView(binding.root)
    }

    override fun onNameEntered(name: String) {
        val route = Route(name)
        if (storageHelper.routeAlreadyExists(route)) {
            AlertDialog.Builder(this)
                .setMessage(R.string.overwritting_route)
                .setPositiveButton(R.string.no) { _, _ ->
                    RouteNameDialog.newInstance().apply {
                        show(supportFragmentManager, ROUTE_NAME_DIALOG)
                    }
                }
                .setNegativeButton(R.string.yes) { _, _ ->
                    storageHelper.removeRouteByName(name)
                    setupRouteInViewAndModel(route)
                }
                .show()
        } else {
            setupRouteInViewAndModel(route)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_PHOTO) {
            if (Activity.RESULT_OK == resultCode) {
                Toast.makeText(applicationContext, R.string.photo_saving, Toast.LENGTH_SHORT).show()
                val photoTempFile = getPhotoTempFile()
                val photoHelper = PhotoHelper(storageHelper)
                val fileForPhoto = model.photoFileForTreasureNeedingPhoto(storageHelper)
                if (fileForPhoto != null && photoHelper.rescaleImageAndSaveInTreasure(photoTempFile, fileForPhoto)) {
                    Toast.makeText(applicationContext, R.string.photo_saved, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(applicationContext, R.string.photo_failed, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun doPhotoForTreasure(treasure: TreasureDescription) {
        model.setupTreasureNeedingPhotoById(treasure.id)
        val photoUri = createPhotoUri()

        val captureImage = capturePhotoIntent()
        captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)

        val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY)
        for (cameraActivity in cameraActivities) {
            grantUriPermission(cameraActivity.activityInfo.packageName, photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }
        startActivityForResult(captureImage, REQUEST_PHOTO)
    }

    private fun addTreasureListener() {
        val treasure = TreasureDescription(
            id = model.nextTreasureId(),
            latitude = binding.editorLatValue.text.toString().toDouble(),
            longitude = binding.editorLongValue.text.toString().toDouble()
        )
        model.addTreasure(treasure, storageHelper)
        treasureAdapter.notifyDataSetChanged()
    }

    private fun createPhotoUri(): Uri {
        val photoFile = getPhotoTempFile()
        if (!photoFile.exists()) {
            photoFile.createNewFile()
        }
        return FileProvider.getUriForFile(this, "pl.marianjureczko.poszukiwacz.fileprovider", photoFile)
    }

    private fun restoreState(savedInstanceState: Bundle?) {
        //when savedInstanceState exists we restore it regardless if it is editing existing or creating a new route
        if (savedInstanceState != null) {
            model.initializeFromState(storageHelper)
            if (model.routeNameWasInitialized()) {
                setupTreasureView(model.getRoute())
            }
        } else {
            val existingRouteName = intent.getStringExtra(SELECTED_ROUTE)
            if (isInEditExistingRouteMode(existingRouteName)) {
                model.initialize(existingRouteName!!, storageHelper)
                setupTreasureView(model.getRoute())
            } else {
                showRouteNameDialog()
            }
        }
    }

    private fun showRouteNameDialog() {
        RouteNameDialog.newInstance().apply {
            show(supportFragmentManager, ROUTE_NAME_DIALOG)
        }
    }

    private fun isInEditExistingRouteMode(existingRouteName: String?) =
        existingRouteName != null

    private fun getPhotoTempFile() = File(storageHelper.pathToRoutesDir() + TMP_PICTURE_FILE)

    private fun capturePhotoIntent() = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

    private fun setupRouteInViewAndModel(route: Route) {
        model.setRoute(route)
        setupTreasureView(route)
    }

    private fun setupTreasureView(route: Route) {
        treasureAdapter = TreasureAdapter(this, route, storageHelper, this)
        binding.treasures.adapter = treasureAdapter
        supportActionBar?.title = "${App.getResources().getString(R.string.route)} ${route.name}"
    }
}
