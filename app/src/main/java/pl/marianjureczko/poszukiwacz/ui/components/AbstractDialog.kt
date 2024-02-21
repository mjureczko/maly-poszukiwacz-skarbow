package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.border
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.ui.theme.Primary

@Composable
fun AbstractDialog(
    state: Boolean,
    hideIt: () -> Unit,
    title: Int,
    text: @Composable (() -> Unit)? = null,
    buttons: @Composable () -> Unit
) {
    if (state) {
        AlertDialog(
            onDismissRequest = { hideIt() },
            modifier = Modifier.border(width = 1.dp, color = Primary),
            title = { Text(text = App.getResources().getString(title)) },
            text = text,
            buttons = buttons
        )
    }
}