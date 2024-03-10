package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.App

@Composable
fun MenuEntry(drawableId: Int, textId: Int, onClick: () -> Unit) {
    DropdownMenuItem(onClick = onClick) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painterResource(drawableId),
                contentDescription = null,
            )
            Text(
                text = App.getResources().getString(textId),
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}