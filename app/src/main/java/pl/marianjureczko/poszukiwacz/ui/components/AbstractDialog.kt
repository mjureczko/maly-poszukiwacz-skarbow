package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AbstractDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    title: Int?,
    titleString: String? = null,
    text: @Composable (() -> Unit)? = null,
    titleDescription: String = "",
    buttons: @Composable () -> Unit
) {
    if (visible) {
        val context = LocalContext.current
        val titleToShow = titleString ?: context.resources.getString(title!!)
        AlertDialog(
            onDismissRequest = { hideIt() },
            title = {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics {
                            contentDescription = titleDescription
                        },
                    text = titleToShow,
                    style = MaterialTheme.typography.headlineLarge,
                    textAlign = TextAlign.Center
                )
            },
            text = text ?: {},
            confirmButton = buttons,
        )
    }
}