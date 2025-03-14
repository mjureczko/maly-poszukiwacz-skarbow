package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

@Composable
fun OkDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    content: @Composable (() -> Unit)?,
) {
    if (visible) {
        AlertDialog(
            modifier = Modifier.border(width = 1.dp, color = colorResource(R.color.colorPrimary)),
            onDismissRequest = { hideIt() },
            buttons = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 16.dp)
                ) {
                    content?.invoke()
                    Row(
                        modifier = Modifier
                            .padding(all = 16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = { hideIt() }) {
                            Text(stringResource(R.string.ok))
                        }
                    }
                }
            },
            shape = RectangleShape
        )
    }

}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun OkDialogPreview() {
    OkDialog(true, {}) {
        Column {
            Row() {
                Text("A")
                Image(
                    painterResource(R.drawable.chest_small),
                    contentDescription = "",
                )
                Text("B")
            }
            Text("CCCCCC")
        }
    }
}