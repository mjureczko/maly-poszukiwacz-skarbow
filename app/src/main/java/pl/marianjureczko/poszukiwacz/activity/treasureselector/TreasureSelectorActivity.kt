package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.location.Location
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.treasureseditor.TreasureAdapter
import pl.marianjureczko.poszukiwacz.databinding.ActivityTreasureSelectorBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.model.TreasureDescription
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TreasureSelectorActivity : ActivityWithAdsAndBackButton(), ActivityTerminator {

    private lateinit var binding: ActivityTreasureSelectorBinding
    private val model: SelectorViewModel by viewModels()
    private lateinit var adapter: TreasureProgressAdapter

    companion object {
        const val NON_SELECTED = -1
        const val RESULT = "pl.marianjureczko.poszukiwacz.activity.treasure_selector_result"
        internal const val ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_to_select_from"
        internal const val PROGRESS = "pl.marianjureczko.poszukiwacz.activity.route_progress"
        internal const val SELECTED_TREASURE = "pl.marianjureczko.poszukiwacz.activity.selected_treasure"
        internal const val LOCATION = "pl.marianjureczko.poszukiwacz.activity.user_coordinates"
        private val xmlHelper = XmlHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTreasureSelectorBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.treasuresToSelect.layoutManager = LinearLayoutManager(this)

        model.route = xmlHelper.loadFromString<Route>(intent.getStringExtra(ROUTE)!!)
        model.progress = xmlHelper.loadFromString<TreasureBag>(intent.getStringExtra(PROGRESS)!!)
        model.selectedTreasure = intent.getIntExtra(SELECTED_TREASURE, NON_SELECTED)
        model.userLocation = intent.getSerializableExtra(LOCATION) as Coordinates?

        adapter = TreasureProgressAdapter(this, model, this)
        binding.treasuresToSelect.adapter = adapter
        supportActionBar?.title = "${App.getResources().getString(R.string.select_treasure_dialog_title)}"

        setContentView(binding.root)
    }

    override fun finishWithResult(treasureId: Int) {
        val data = Intent()
        data.putExtra(RESULT, treasureId)
        setResult(Activity.RESULT_OK, data)
        finish()
    }

}