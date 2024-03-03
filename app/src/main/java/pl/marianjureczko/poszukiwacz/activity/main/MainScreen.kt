package pl.marianjureczko.poszukiwacz.activity.main

import android.annotation.SuppressLint
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(isClassic: Boolean, onClickOnGuide: () -> Unit, goToSearching: (String) -> Unit) {
    Scaffold(
        topBar = { TopBar(onClickOnGuide) },
        content = { _ ->
            if (isClassic) {
                ClassicScreenBody(goToSearching)
            } else {
                CustomScreenBody(goToSearching)
            }
        }
    )
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun DefaultPreview() {
    AppTheme {
        MainScreen(false, {}, {})
    }
}

