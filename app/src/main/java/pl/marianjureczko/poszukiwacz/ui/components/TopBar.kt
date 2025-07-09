package pl.marianjureczko.poszukiwacz.ui.components

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

/**
 * Do not show entries for onClickHandlers that are null.
 */
data class MenuConfig(
    val onClickOnGuide: (() -> Unit)?,
    val onClickOnFacebook: (() -> Unit)? = null,
    val onClickOnRestart: (ViewModelProgressRestarter)? = null
)

fun interface ViewModelProgressRestarter {
    operator fun invoke()
}

@Composable
fun TopBar(
    navController: NavController,
    title: String,
    menuConfig: MenuConfig
) {
    val showMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current
    TopAppBar(
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                EmbeddedButton(
                    modifier = Modifier.semantics { contentDescription = TOPBAR_GO_BACK },
                    imageVector = Icons.Outlined.ArrowBack,
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
            IconButton(onClick = { showMenu.value = !showMenu.value }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Color.White)
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(0.dp))) {
                DropdownMenu(
                    expanded = showMenu.value,
                    onDismissRequest = { showMenu.value = false }
                ) {
                    menuConfig.onClickOnGuide?.let {
                        MenuEntry(R.drawable.question_mark, R.string.menu_help, it)
                    }
                    menuConfig.onClickOnFacebook?.let {
                        MenuEntry(R.drawable.facebook, R.string.menu_facebook, it)
                    }
                    menuConfig.onClickOnRestart?.let { _ ->
                        MenuEntry(R.drawable.restart, R.string.menu_restart) {
                            showMenu.value = false
                            showRestartDialog.value = true
                        }
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
                    style = MaterialTheme.typography.h6.copy(fontFamily = FontFamily.Default),
                    textAlign = TextAlign.Center
                )
            }
        })
}