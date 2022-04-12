package pl.marianjureczko.poszukiwacz.activity.treasureselector

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityTreasureSelectorBinding
import pl.marianjureczko.poszukiwacz.model.Route
import pl.marianjureczko.poszukiwacz.model.TreasureBag
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton
import pl.marianjureczko.poszukiwacz.shared.XmlHelper

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class TreasureSelectorActivity : ActivityWithAdsAndBackButton(), ActivityTerminator {

    private val TAG = javaClass.simpleName
    private lateinit var binding: ActivityTreasureSelectorBinding
    private val model: SelectorViewModel by viewModels()
    private lateinit var adapter: TreasureProgressAdapter

    companion object {
        const val RESULT_PROGRESS = "pl.marianjureczko.poszukiwacz.activity.treasure_selector_result_progress"
        internal const val ROUTE = "pl.marianjureczko.poszukiwacz.activity.route_to_select_from"
        internal const val PROGRESS = "pl.marianjureczko.poszukiwacz.activity.route_progress"
        internal const val LOCATION = "pl.marianjureczko.poszukiwacz.activity.user_coordinates"
        private val xmlHelper = XmlHelper()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityTreasureSelectorBinding.inflate(layoutInflater)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding.treasuresToSelect.layoutManager = LinearLayoutManager(this)

        model.initialize(
            route = xmlHelper.loadFromString<Route>(intent.getStringExtra(ROUTE)!!),
            progress = xmlHelper.loadFromString<TreasureBag>(intent.getStringExtra(PROGRESS)!!),
            userLocation = intent.getSerializableExtra(LOCATION) as Coordinates?
        )
        adapter = TreasureProgressAdapter(this, model, this)
        binding.treasuresToSelect.adapter = adapter
        supportActionBar?.title = "${App.getResources().getString(R.string.select_treasure_dialog_title)}"

        setContentView(binding.root)
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

    private fun intentResultWithProgress(): Intent {
        val data = Intent()
        data.putExtra(RESULT_PROGRESS, model.progressToString(xmlHelper))
        return data
    }

}