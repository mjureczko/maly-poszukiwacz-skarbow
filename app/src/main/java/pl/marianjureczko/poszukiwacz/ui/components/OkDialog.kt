package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes
import pl.marianjureczko.poszukiwacz.ui.theme.buttonColors

@Composable
fun OkDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    content: @Composable (() -> Unit)?,
) {
    if (visible) {
        AbstractDialog(
            visible = visible,
            hideIt = { hideIt() },
            text = content,
            title = null,
            titleString = "",
            buttons = {
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        shape = Shapes.large,
                        colors = buttonColors(),
                        onClick = { hideIt() }) {
                        Text(text = stringResource(R.string.ok))
                    }
                }
            }
        )
    }

}
