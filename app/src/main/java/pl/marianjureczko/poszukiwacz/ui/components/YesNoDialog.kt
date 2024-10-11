package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

@Composable
fun YesNoDialog(state: Boolean, hideIt: () -> Unit, title: Int? = null, titleString: String? = null, onYes: () -> Unit) {
    AbstractDialog(state, hideIt, title, titleString,
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
                    Text(stringResource(R.string.yes))
                }
                TextButton(onClick = { hideIt() }) {
                    Text(stringResource(R.string.no))
                }
            }
        }
    )
}