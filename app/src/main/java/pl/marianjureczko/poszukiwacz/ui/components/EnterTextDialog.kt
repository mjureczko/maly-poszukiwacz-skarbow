package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

@Composable
fun EnterTextDialog(
    state: Boolean,
    hideIt: () -> Unit,
    title: Int,
    onClick: (text: String) -> Unit
) {
    val text = remember { mutableStateOf("") }
    AbstractDialog(
        state,
        { text.value = ""; hideIt() },
        title,
        text = {
            Column {
                TextField(
                    value = text.value,
                    onValueChange = { text.value = it }
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier.padding(all = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    shape = CutCornerShape(percent = 25),
                    modifier = Modifier.fillMaxWidth(),
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
}