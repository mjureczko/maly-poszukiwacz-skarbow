package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavController?,
    isClassic: Boolean,
    resources: Resources,
    onClickOnGuide: () -> Unit,
    goToSearching: (String) -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide) },
        content = { _ ->
            if (isClassic) {
                ClassicScreenBody(goToSearching)
            } else {
                CustomScreenBody(resources, goToSearching)
            }
        }
    )
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun DefaultPreview() {
    AppTheme {
        MainScreen(null, false, App.getResources(), {}, {})
    }
}

