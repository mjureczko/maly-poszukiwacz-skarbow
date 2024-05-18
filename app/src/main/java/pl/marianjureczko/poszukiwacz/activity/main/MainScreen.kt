package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.ui.components.TopBar

//TODO: add sample qr code to introduction
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController,
    isClassic: Boolean,
    resources: Resources,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit,
    goToSearching: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide, onClickOnFacebook) },
        content = { _ ->
            if (isClassic) {
                ClassicScreenBody(goToSearching)
            } else {
                CustomScreenBody(resources, goToSearching)
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
