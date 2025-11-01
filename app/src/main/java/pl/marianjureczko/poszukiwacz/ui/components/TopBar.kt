package pl.marianjureczko.poszukiwacz.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R

const val TOPBAR_SCREEN_TITLE = "Screen title"
const val TOPBAR_GO_BACK = "Go back"
const val TOPBAR_MENU_BUTTON = "Open menu"
const val TOPBAR_MENU_RESTART = "Restart menu entry"

/**
 * Do not show entries for onClickHandlers that are null.
 */
data class MenuConfig(
    val onClickOnGuide: (() -> Unit)?,
    val onClickOnFacebook: (() -> Unit)? = null,
    val onClickOnRestart: ViewModelProgressRestarter? = null,
    val onClickBadges: GoToBadgesScreen? = null,
) {

}


fun interface ViewModelProgressRestarter {
    operator fun invoke()
}

fun interface GoToBadgesScreen {
    operator fun invoke()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    title: String,
    menuConfig: MenuConfig
) {
    val showMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(
        windowInsets = WindowInsets.statusBars,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.colorPrimary)
        ),
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                EmbeddedButton(
                    modifier = Modifier.semantics { contentDescription = TOPBAR_GO_BACK },
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    colorFilter = ColorFilter.tint(Color.White)
                ) { navController.navigateUp() }
            }
        },
        actions = {
            val showRestartDialog = remember { mutableStateOf(false) }
            YesNoDialog(
                showRestartDialog.value,
                hideIt = { showRestartDialog.value = false },
                title = R.string.restart_msg,
            ) {
                showRestartDialog.value = false
                menuConfig.onClickOnRestart!!()
                Toast.makeText(context, R.string.restart_confirmation, Toast.LENGTH_LONG).show()
            }
            IconButton(
                onClick = { showMenu.value = !showMenu.value },
                modifier = Modifier.semantics { contentDescription = TOPBAR_MENU_BUTTON }
            ) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Color.White)
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(0.dp))) {
                DropdownMenu(
                    expanded = showMenu.value,
                    onDismissRequest = { showMenu.value = false }
                ) {
                    menuConfig.onClickOnGuide?.let {
                        MenuEntry(R.drawable.question_mark, R.string.menu_help, onClick = it)
                    }
                    menuConfig.onClickOnFacebook?.let {
                        MenuEntry(R.drawable.facebook, R.string.menu_facebook, onClick = it)
                    }
                    menuConfig.onClickOnRestart?.let { _ ->
                        MenuEntry(
                            R.drawable.restart,
                            R.string.menu_restart,
                            modifier = Modifier.semantics { contentDescription = TOPBAR_MENU_RESTART }
                        ) {
                            showMenu.value = false
                            showRestartDialog.value = true
                        }
                    }
                    menuConfig.onClickBadges?.let { goToBadgesScreen ->
                        MenuEntry(
                            drawableId = R.drawable.icon_badges,
                            textId = R.string.achievements,
                            modifier = Modifier.semantics { contentDescription = "TODO" },
                            onClick = { goToBadgesScreen() },
                        )
                    }
                }
            }
        },

        title = {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(1.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painterResource(R.drawable.chest_very_small),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight(0.8f),
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    modifier = Modifier.semantics { contentDescription = TOPBAR_SCREEN_TITLE },
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.titleLarge.copy(fontFamily = FontFamily.Default),
                    textAlign = TextAlign.Center
                )
            }
        })
}
