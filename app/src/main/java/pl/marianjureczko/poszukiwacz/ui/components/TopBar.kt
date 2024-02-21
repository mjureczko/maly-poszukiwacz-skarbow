package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.theme.Primary

@Composable
fun TopBar(onClickOnGuide: () -> Unit) {
    val showMenu = remember { mutableStateOf(false) }
    TopAppBar(
        backgroundColor = Primary,
        navigationIcon = {
            EmbeddedButton(
                Icons.Outlined.ArrowBack,
                ColorFilter.tint(Color.White)
            ) { print("TODO back") }
        },
        actions = {
            IconButton(onClick = { showMenu.value = !showMenu.value }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = null, tint = Color.White)
            }
            DropdownMenu(
                expanded = showMenu.value,
                onDismissRequest = { showMenu.value = false }
            ) {
                //TODO extract to dedicated file custom menu entry
                DropdownMenuItem(onClick = onClickOnGuide) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painterResource(R.drawable.question_mark),
                            contentDescription = null,
                        )
                        Text(
                            text = App.getResources().getString(R.string.menu_help),
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                }
                DropdownMenuItem(onClick = { onClickOnGuide }) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painterResource(R.drawable.facebook),
                            contentDescription = null,
                        )
                        Text(
                            text = App.getResources().getString(R.string.menu_facebook),
                            modifier = Modifier.padding(8.dp)
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
                    text = App.getResources().getString(R.string.app_name),
                    //TODO save color in theme
                    color = Color.White,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )
            }
        })
}