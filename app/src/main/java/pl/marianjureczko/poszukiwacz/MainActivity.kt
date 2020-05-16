package pl.marianjureczko.poszukiwacz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    companion object {
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle?) {
        println("########> onSaveInstanceState ${System.currentTimeMillis() % 100_000}")
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        println("########> onCreate ${System.currentTimeMillis() % 100_000}")
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        dummy_button.setOnTouchListener(mDelayHideTouchListener)
    }

}

