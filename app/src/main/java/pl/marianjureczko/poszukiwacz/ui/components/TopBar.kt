package pl.marianjureczko.poszukiwacz.ui.components

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R

@Composable
fun TopBar(navController: NavController, title: String, onClickOnGuide: () -> Unit, onClickOnFacebook: () -> Unit) {
    val showMenu = remember { mutableStateOf(false) }
    TopAppBar(
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                EmbeddedButton(
                    Icons.Outlined.ArrowBack,
                    ColorFilter.tint(Color.White)
                ) { navController.navigateUp() }
            }
        },
        actions = {
            IconButton(onClick = { showMenu.value = !showMenu.value }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Color.White)
            }
            MaterialTheme(shapes = MaterialTheme.shapes.copy(medium = RoundedCornerShape(0.dp))) {
                DropdownMenu(
                    expanded = showMenu.value,
                    onDismissRequest = { showMenu.value = false }
                ) {
                    MenuEntry(R.drawable.question_mark, R.string.menu_help, onClickOnGuide)
                    MenuEntry(R.drawable.facebook, R.string.menu_facebook, onClickOnFacebook)
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
                    text = title,
                    color = Color.White,
                    style = MaterialTheme.typography.h6.copy(fontFamily = FontFamily.Default),
                    textAlign = TextAlign.Center
                )
            }
        })
}