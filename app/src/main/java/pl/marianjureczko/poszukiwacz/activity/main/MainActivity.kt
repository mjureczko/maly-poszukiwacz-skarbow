package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.commemorative.n.PARAMETER_TREASURE_DESCRIPTION_ID
import pl.marianjureczko.poszukiwacz.activity.searching.n.PARAMETER_ROUTE_NAME
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_RESULT_TYPE
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_AMOUNT
import pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_TREASURE_ID
import pl.marianjureczko.poszukiwacz.screen.treasureselector.PARAMETER_JUST_FOUND_TREASURE
import pl.marianjureczko.poszukiwacz.ui.ComposeRoot
import pl.marianjureczko.poszukiwacz.ui.Screen
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme

val TREASURE_EDITOR_PATH = "treasureeditor"
val TREASURE_EDITOR_ROUTE = "$TREASURE_EDITOR_PATH/{$PARAMETER_ROUTE_NAME}"
val SEARCHING_PATH = "searching"
val SEARCHING_ROUTE = "$SEARCHING_PATH/{$PARAMETER_ROUTE_NAME}"
val RESULTS_PATH = "result"
val RESULTS_ROUTE = "$RESULTS_PATH/{${pl.marianjureczko.poszukiwacz.screen.result.PARAMETER_ROUTE_NAME}}/{$PARAMETER_RESULT_TYPE}/{$PARAMETER_TREASURE_ID}/{$PARAMETER_TREASURE_AMOUNT}"
val SELECTOR_PATH = "selector"
val SELECTOR_ROUTE = "$SELECTOR_PATH/{$PARAMETER_JUST_FOUND_TREASURE}"
val COMMEMORATIVE_PATH = "commemorative"
val COMMEMORATIVE_ROUTE = "$COMMEMORATIVE_PATH/{$PARAMETER_TREASURE_DESCRIPTION_ID}"
val FACEBOOK_PATH = "facebook"
val FACEBOOK_ROUTE = "$FACEBOOK_PATH/{${pl.marianjureczko.poszukiwacz.screen.facebook.PARAMETER_ROUTE_NAME}}"

/**
 * Routes creation and selection activity
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val TAG = javaClass.simpleName

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        Screen.init(this)

        setContent {
            AppTheme {
                ComposeRoot { onClickOnGuide() }
            }
        }
    }

    override fun onPause() {
        Log.i(TAG, "onPause")
        super.onPause()
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        super.onResume()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

    fun onClickOnGuide() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(resources.getString(R.string.help_path))))
    }
}
