package pl.marianjureczko.poszukiwacz.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@Composable
fun LargeButton(title: String, description: String = "", enabled: Boolean = true, onClick: () -> Unit) {
    val contentColor = if (enabled) {
        Color.Black
    } else {
        Color.Gray
    }
    OutlinedButton(
        enabled = enabled,
        shape = Shapes.large,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { contentDescription = description }
            .clickable { Log.d("LargeButton", "LargeButton: ") },
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.LightGray,
            contentColor = contentColor
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        onClick = onClick
    ) {
        Text(
            title,
            color = contentColor
        )
    }
}

@Composable
fun LargeButton(title: Int, description: String = "", enabled: Boolean = true, onClick: () -> Unit) {
    LargeButton(stringResource(title), description, enabled, onClick)
}