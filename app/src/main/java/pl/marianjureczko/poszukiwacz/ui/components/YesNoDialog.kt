package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R

@Composable
fun YesNoDialog(state: Boolean, hideIt: () -> Unit, title: Int, onYes: () -> Unit) {
    AbstractDialog(state, hideIt, title,
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {
                    onYes()
                    hideIt()
                }) {
                    Text(App.getResources().getString(R.string.yes))
                }
                TextButton(onClick = { hideIt() }) {
                    Text(App.getResources().getString(R.string.no))
                }
            }
        }
    )
}