package pl.marianjureczko.poszukiwacz.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@Composable
fun EnterTextDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    title: Int,
    textFieldDescription: String = "",
    buttonDescription: String = "",
    titleDescription: String = "",
    onClick: (text: String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    AbstractDialog(
        visible,
        { text.value = ""; hideIt() },
        title,
        titleDescription = titleDescription,
        text = {
            Column {
                TextField(
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .semantics {
                            contentDescription = textFieldDescription
                        },
                    textStyle = MaterialTheme.typography.body1,
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
                    modifier = Modifier
                        .width(140.dp)
                        .semantics {
                            contentDescription = buttonDescription
                        },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.LightGray),
                    onClick = {
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
            try {
                focusRequester.requestFocus()
            } catch (e: Exception) {
                Log.e("EnterTextDialog", "Ignoring error, we can go on without focus: ${e.message}", )
            }
        }
        DisposableEffect(key1 = "on dispose") {
            onDispose {
                try {
                    focusManager.clearFocus()
                } catch (e: Exception) {
                    Log.e("EnterTextDialog", "Ignoring error, we can go on without clearing focus: ${e.message}", )
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun EnterTextDialogPreview() {
    EnterTextDialog(true, {}, R.string.app_name) {}
}