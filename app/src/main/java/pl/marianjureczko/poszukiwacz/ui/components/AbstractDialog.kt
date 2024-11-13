package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

@Composable
fun AbstractDialog(
    visible: Boolean,
    hideIt: () -> Unit,
    title: Int?,
    titleString: String? = null,
    text: @Composable (() -> Unit)? = null,
    buttons: @Composable () -> Unit
) {
    if (visible) {
        val context = LocalContext.current
        val titleToShow = titleString ?: context.resources.getString(title!!)
        AlertDialog(
            modifier = Modifier.border(width = 1.dp, color = colorResource(R.color.colorPrimary)),
            onDismissRequest = { hideIt() },
            title = { Text(
                modifier = Modifier.fillMaxWidth(),
                text = titleToShow,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Center
            ) },
            text = text,
            buttons = buttons,
            shape = RectangleShape
        )
    }
}