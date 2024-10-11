package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@Composable
fun EnterTextDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    title: Int,
    onClick: (text: String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    AbstractDialog(
        visible,
        { text.value = ""; hideIt() },
        title,
        text = {
            Column {
                TextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    shape = RoundedCornerShape(0),
                    value = text.value,
                    onValueChange = { text.value = it }
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                Button(
                    shape = Shapes.large,
                    modifier = Modifier.width(140.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                    onClick = {
                        focusManager.clearFocus()
                        hideIt()
                        onClick(text.value)
                        text.value = ""
                    },
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        }
    )
    if (visible) {
        LaunchedEffect(key1 = "on start") {
            focusRequester.requestFocus()
        }
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun EnterTextDialogPreview() {
    EnterTextDialog(true, {}, R.string.app_name, {})
}