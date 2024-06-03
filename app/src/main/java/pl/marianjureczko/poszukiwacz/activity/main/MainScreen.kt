package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.permissions.RequirementsForNavigation
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.handlePermissionWithExitOnDenied

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    isClassic: Boolean,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit,
    goToSearching: (String) -> Unit
) {
    handlePermissionWithExitOnDenied(RequirementsForNavigation)
    Scaffold(
        topBar = { TopBar(navController, stringResource(R.string.app_name), onClickOnGuide, onClickOnFacebook) },
        content = { _ ->
            if (isClassic) {
                ClassicScreenBody(goToSearching)
            } else {
                CustomScreenBody(goToSearching)
            }
        }
    )
}

//@Preview(showBackground = true, apiLevel = 31)
//@Composable
//fun DefaultPreview() {
//    AppTheme {
//        MainScreen(null, false, App.getResources(), {}, {})
//    }
//}
