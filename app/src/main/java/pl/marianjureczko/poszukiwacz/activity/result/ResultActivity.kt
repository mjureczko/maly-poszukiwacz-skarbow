package pl.marianjureczko.poszukiwacz.activity.result

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityResultBinding
import pl.marianjureczko.poszukiwacz.model.TreasuresProgress
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton

class ResultActivity : ActivityWithAdsAndBackButton() {

    companion object {
        const val RESULT = "pl.marianjureczko.poszukiwacz.activity.result";
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val input = intent.getSerializableExtra(RESULT) as ResultActivityData
        if (input.isError()) {
            binding.resultDescription.text = input.error
            binding.resultImg.visibility = View.GONE
        } else {
            binding.resultDescription.text = input.treasure!!.quantity.toString()
            binding.resultImg.setImageResource(input.treasure.type.image())
        }
        setContentView(binding.root)
        setUpAds(binding.adView)
    }

    override fun getCurrentTreasuresProgress(): TreasuresProgress? {
        return null
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }
}