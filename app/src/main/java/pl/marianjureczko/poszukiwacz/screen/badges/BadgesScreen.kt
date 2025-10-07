package pl.marianjureczko.poszukiwacz.screen.badges

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar

@Composable
fun BadgesScreen(
    navController: NavController,
    onClickOnGuide: GoToGuide,
) {
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(id = R.string.achievements),
                menuConfig = MenuConfig(onClickOnGuide),
            )
        },
        content = { paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)) {

            }
        }
    )
}