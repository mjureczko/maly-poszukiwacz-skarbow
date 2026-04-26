package pl.marianjureczko.poszukiwacz.screen.facebook

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.screen.Screens
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGallery

object FacebookHelper {
    private const val FACEBOOK_PACKAGE = "com.facebook.katana"

    @Composable
    fun createFacebookCallback(navController: NavHostController): GoToFacebook {
        val context = LocalContext.current
        val noFacebookErrorMsg = stringResource(id = R.string.facebook_share_impossible)
        val goToFacebook = { routeName: String ->
            if (isFacebookInstalled(context)) {
                navController.navigate(Screens.Facebook.doRoute(Mode.FACEBOOK, routeName))
            } else {
                Toast.makeText(context, noFacebookErrorMsg, Toast.LENGTH_LONG).show()
            }
        }
        return goToFacebook
    }

    @Composable
    fun createGalleryCallback(navController: NavHostController): GoToGallery {
        // No need to check for Facebook being installed when opening in GALLERY mode
        val goToGallery = { routeName: String ->
            navController.navigate(Screens.Facebook.doRoute(Mode.GALLERY, routeName))
        }
        return goToGallery
    }

    private fun isFacebookInstalled(context: Context): Boolean {
        val packageManager = context.packageManager
        return try {
            //the not deprecated version requires API 33
            packageManager.getPackageInfo(FACEBOOK_PACKAGE, PackageManager.GET_ACTIVITIES)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }
}