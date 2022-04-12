package pl.marianjureczko.poszukiwacz.activity.result

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.databinding.ActivityResultBinding
import pl.marianjureczko.poszukiwacz.shared.ActivityWithAdsAndBackButton

class ResultActivity : ActivityWithAdsAndBackButton() {

    companion object {
        private const val RESULT = "pl.marianjureczko.poszukiwacz.activity.result";

        fun intent(packageContext: Context, treasure: ResultActivityInput) =
            Intent(packageContext, ResultActivity::class.java).apply {
                putExtra(RESULT, treasure)
            }
    }

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        binding = ActivityResultBinding.inflate(layoutInflater)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContentView(R.layout.activity_result)
        val input = intent.getSerializableExtra(RESULT) as ResultActivityInput
        if (input.isError()) {
            binding.resultDescription.text = input.error
            binding.resultImg.visibility = View.GONE
        } else {
            binding.resultDescription.text = input.treasure!!.quantity.toString()
            binding.resultImg.setImageResource(input.treasure.type.image())
        }
        setContentView(binding.root)
    }
}