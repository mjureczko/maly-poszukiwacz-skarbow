package pl.marianjureczko.poszukiwacz.activity.treasureseditor

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mapbox.maps.MapView
import kotlinx.coroutines.launch
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityTreasuresEditorBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForPhotoAndAudioTip
import pl.marianjureczko.poszukiwacz.shared.LocationRequester
import pl.marianjureczko.poszukiwacz.shared.MapHelper
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper

private const val ROUTE_NAME_DIALOG = "RouteNameDialog"

class TreasuresEditorActivity : PermissionActivity(), RouteNameDialog.Callback, TreasurePhotoMaker, TreasureRemover {

    companion object {
        const val TMP_PICTURE_FILE = "/tmp.jpg"
        private const val SELECTED_ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_selected_to_edit";

        fun intent(packageContext: Context) = Intent(packageContext, TreasuresEditorActivity::class.java)

        fun intent(packageContext: Context, routeName: String) =
            Intent(packageContext, TreasuresEditorActivity::class.java).apply {
                putExtra(SELECTED_ROUTE, routeName)
            }
    }

    private val TAG = javaClass.simpleName
    override fun getTreasureProgress(): TreasuresProgress? {
        return null
    }

    private val storageHelper = StorageHelper(this)
    private val photoHelper = PhotoHelper(this, storageHelper)
    private val doPhotoLauncher = registerForActivityResult(TakePicture()) { result ->
        if (result) {
            savePhotoInRoute()
        }
    }

    lateinit var treasureAdapter: TreasureAdapter
    private lateinit var binding: ActivityTreasuresEditorBinding
    private lateinit var mapView: MapView

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

        mapView = binding.mapView
        MapHelper.renderTreasures(model.getRoute(), mapView, this.resources)

        setContentView(binding.root)
        setUpAds(binding.adView)
    }

    override fun onResume() {
        super.onResume()
        MapHelper.positionMapOnTreasures(model.getRoute(), mapView, 200.0)
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

    override fun doPhotoForTreasure(treasure: TreasureDescription) {
        model.setupTreasureNeedingPhotoById(treasure.id)
        doPhotoLauncher.launch(photoHelper.createTipPhotoUri())
    }

    private fun addTreasureListener() {
        val treasure = TreasureDescription(
            id = model.nextTreasureId(),
            latitude = binding.editorLatValue.text.toString().toDouble(),
            longitude = binding.editorLongValue.text.toString().toDouble()
        )
        model.addTreasure(treasure, storageHelper)
        treasureAdapter.notifyDataSetChanged()
        MapHelper.addTreasureToMap(treasure, mapView, this.resources)
        MapHelper.positionMapOnTreasures(model.getRoute(), mapView, 0.0)
    }

    private fun savePhotoInRoute() {
        Toast.makeText(applicationContext, R.string.photo_saving, Toast.LENGTH_SHORT).show()
        val photoTempFile = photoHelper.getPhotoTempFile()
        val fileForPhoto = model.photoFileForTreasureNeedingPhoto(storageHelper)
        if (fileForPhoto != null) {
            lifecycleScope.launch {
                photoHelper.rescaleImageAndSaveInTreasure(
                    photoTempFile,
                    fileForPhoto,
                    onSuccess = {
                        model.saveRoute(storageHelper)
                        Toast.makeText(applicationContext, R.string.photo_saved, Toast.LENGTH_SHORT).show()
                    },
                    onFailure = { photoSavingError() })
            }
        } else {
            photoSavingError()
        }
    }

    private fun photoSavingError() {
        Toast.makeText(applicationContext, R.string.photo_failed, Toast.LENGTH_SHORT).show()
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

    private fun setupRouteInViewAndModel(route: Route) {
        model.setRoute(route)
        setupTreasureView(route)
    }

    private fun setupTreasureView(route: Route) {
        treasureAdapter = TreasureAdapter(this, route, storageHelper, this, this)
        binding.treasures.adapter = treasureAdapter
        supportActionBar?.title = "${App.getResources().getString(R.string.route)} ${route.name}"
    }

    override fun remove(treasureToRemove: TreasureDescription) {
        model.removeTreasure(treasureToRemove, storageHelper)
        treasureAdapter.notifyDataSetChanged()
        MapHelper.renderTreasures(model.getRoute(), mapView, this.resources)
        MapHelper.positionMapOnTreasures(model.getRoute(), mapView, 0.0)
    }
}
