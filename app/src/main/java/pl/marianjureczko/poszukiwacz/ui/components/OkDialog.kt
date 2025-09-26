package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

@OptIn(ExperimentalMaterial3Api::class)
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
            //TODO t:
//            buttons = {
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(all = 16.dp)
//                ) {
//                    content?.invoke()
//                    Row(
//                        modifier = Modifier
//                            .padding(all = 16.dp)
//                            .fillMaxWidth(),
//                        horizontalArrangement = Arrangement.Center,
//                        verticalAlignment = Alignment.CenterVertically
//                    ) {
//                        TextButton(onClick = { hideIt() }) {
//                            Text(stringResource(R.string.ok))
//                        }
//                    }
//                }
//            },
//            shape = RectangleShape
            content = { Text("TODO") }
        )
    }

}

@Preview(showBackground = true, apiLevel = 35)
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