package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeContract
import pl.marianjureczko.poszukiwacz.activity.commemorative.CommemorativeInputData
import pl.marianjureczko.poszukiwacz.databinding.ActivityTreasureSelectorBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.permissions.ActivityRequirements
import pl.marianjureczko.poszukiwacz.permissions.PermissionActivity
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForDoingPhoto
import pl.marianjureczko.poszukiwacz.shared.PhotoHelper
import pl.marianjureczko.poszukiwacz.shared.StorageHelper
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TreasureSelectorActivity : PermissionActivity(), ActivityTerminator {

    private val TAG = javaClass.simpleName
    override fun getTreasureProgress(): TreasuresProgress? {
        return model.progress
    }

    private lateinit var binding: ActivityTreasureSelectorBinding
    private val model: SelectorViewModel by viewModels()
    private lateinit var adapter: TreasureProgressAdapter
    private val photoHelper = PhotoHelper(this, StorageHelper(this))
    private val doPhotoLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { result ->
            if (result) {
                val newPhotoLocation = photoHelper.moveCommemorativePhotoToPermanentLocation()
                model.setCommemorativePhotoOnSelectedTreasureDescription(newPhotoLocation)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, R.string.photo_saved, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, R.string.photo_not_saved, Toast.LENGTH_SHORT).show()
            }
        }
    private val commemorativeLauncher: ActivityResultLauncher<CommemorativeInputData> =
        registerForActivityResult(CommemorativeContract()) {}

    companion object {
        const val RESULT_PROGRESS = "pl.marianjureczko.poszukiwacz.activity.treasure_selector_result_progress"
        internal const val ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_to_select_from"
        internal const val PROGRESS = "pl.marianjureczko.poszukiwacz.activity.route_progress"
        internal const val LOCATION = "pl.marianjureczko.poszukiwacz.activity.user_coordinates"
        internal const val NEW_TREASURE_COLLECTED = "pl.marianjureczko.poszukiwacz.activity.new_treasure_collected"
        internal const val TREASURE_DESCRIPTION =
            "pl.marianjureczko.poszukiwacz.activity.treasure_selector_treasure_description"
        private val xmlHelper = XmlHelper()
    }

    override fun onPermissionsGranted(activityRequirements: ActivityRequirements) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityTreasureSelectorBinding.inflate(layoutInflater)
        binding.treasuresToSelect.layoutManager = LinearLayoutManager(this)

        model.initialize(
            route = xmlHelper.loadFromString<Route>(intent.getStringExtra(ROUTE)!!),
            progress = xmlHelper.loadFromString<TreasuresProgress>(intent.getStringExtra(PROGRESS)!!),
            newTreasureCollected = intent.getBooleanExtra(NEW_TREASURE_COLLECTED, false),
            userLocation = intent.getSerializableExtra(LOCATION) as Coordinates?,
            justFoundTreasureDescription = intent.getSerializableExtra(TREASURE_DESCRIPTION) as TreasureDescription?
        )
        adapter = TreasureProgressAdapter(this, model, this, doPhotoLauncher, commemorativeLauncher)
        binding.treasuresToSelect.adapter = adapter
        supportActionBar?.title = "${App.getResources().getString(R.string.select_treasure_dialog_title)}"

        Handler(Looper.getMainLooper()).postDelayed({ markTreasureIfFound() }, 1000)

        setContentView(binding.root)
        assurePermissionsAreGranted(RequirementsForDoingPhoto, false)
        setUpAds(binding.adView)
    }

    override fun finishWithResult(treasureId: Int) {
        model.selectTreasureById(treasureId)
        val data = intentResultWithProgress()
        setResult(Activity.RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
        val data = intentResultWithProgress()
        setResult(Activity.RESULT_CANCELED, data)
        super.onBackPressed()
    }

    private fun markTreasureIfFound() {
        if (model.treasureDescriptionHasBeenIdentified()) {
            val id = model.justFoundTreasureDescription!!.id.toString()
            Toast.makeText(this, this.getString(R.string.treasure_marked_as_collected, id), Toast.LENGTH_LONG).show()
            model.justFoundTreasureDescription = null
            adapter.notifyDataSetChanged()
        } else {
            if (model.newTreasureCollected) {
                //TODO: in case the toast is too quick - https://www.geeksforgeeks.org/display-toast-for-a-specific-time-in-android/
                Toast.makeText(this, R.string.treasure_nor_marked, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun intentResultWithProgress(): Intent {
        val data = Intent()
        data.putExtra(RESULT_PROGRESS, model.progressToString(xmlHelper))
        return data
    }

}