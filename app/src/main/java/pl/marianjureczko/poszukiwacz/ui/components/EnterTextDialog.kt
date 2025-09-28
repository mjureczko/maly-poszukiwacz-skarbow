package pl.marianjureczko.poszukiwacz.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.errorTone
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes
import pl.marianjureczko.poszukiwacz.ui.theme.buttonColors

private val allowedChars: Set<Char> = setOf(' ', '_', '-')

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
        visible = visible,
        hideIt = { text.value = ""; hideIt() },
        title = title,
        titleDescription = titleDescription,
        text = {
            OutlinedTextField(
                value = text.value,
                onValueChange = { input ->
                    text.value = input.filter { it.isLetterOrDigit() || allowedChars.contains(it) }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .semantics {
                        contentDescription = textFieldDescription
                    },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            )
        },
        buttons =
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Button(
                        shape = Shapes.large,
                        modifier = Modifier
                            .width(140.dp)
                            .semantics { contentDescription = buttonDescription },
                        colors = buttonColors(),
                        onClick = {
                            if (text.value.isNotBlank()) {
                                hideIt()
                                onClick(text.value)
                                text.value = ""
                            } else {
                                errorTone()
                            }
                        }
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
                Log.e("EnterTextDialog", "Ignoring error, we can go on without focus: ${e.message}")
            }
        }
        DisposableEffect(key1 = "on dispose") {
            onDispose {
                try {
                    focusManager.clearFocus()
                } catch (e: Exception) {
                    Log.e("EnterTextDialog", "Ignoring error, we can go on without clearing focus: ${e.message}")
                }
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 35)
@Composable
fun EnterTextDialogPreview() {
    EnterTextDialog(true, {}, R.string.app_name) {}
}