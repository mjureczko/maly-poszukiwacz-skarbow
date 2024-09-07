package pl.marianjureczko.poszukiwacz.activity.facebook.n

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.FACEBOOK_PATH

object FacebookHelper {
    private const val facebookPackage = "com.facebook.katana"
    @Composable
    fun createFacebookCallback(navController: NavHostController): () -> Unit {
        val context = LocalContext.current
        val noFacebookErrorMsg = stringResource(id = R.string.facebook_share_impossible)
        val goToFacebook = {
            if (isFacebookInstalled(context)) {
                navController.navigate(FACEBOOK_PATH)
            } else {
                Toast.makeText(context, noFacebookErrorMsg, Toast.LENGTH_LONG).show()
            }
        }
        return goToFacebook
    }

    private fun isFacebookInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            //the not deprecated version requires API 33
            packageManager.getPackageInfo(facebookPackage, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}