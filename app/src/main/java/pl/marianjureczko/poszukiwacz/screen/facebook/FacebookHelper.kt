package pl.marianjureczko.poszukiwacz.screen.facebook

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
    fun createFacebookCallback(navController: NavHostController): (String) -> Unit {
        val context = LocalContext.current
        val noFacebookErrorMsg = stringResource(id = R.string.facebook_share_impossible)
        val noRouteToShareErrorMsg = noRouteToShareMessage()
        val goToFacebook = { routeName: String ->
            if (isFacebookInstalled(context)) {
                //TODO: disable option in menu instead of showing a toast
                if (routeName.isBlank()) {
                    Toast.makeText(context, noRouteToShareErrorMsg, Toast.LENGTH_LONG).show()
                } else {
                    navController.navigate("$FACEBOOK_PATH/$routeName")
                }
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