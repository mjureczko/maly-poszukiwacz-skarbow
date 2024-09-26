package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@Composable
fun LargeButton(title: Int, description: String = "", enabled: Boolean = true, onClick: () -> Unit) {
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
            .semantics { contentDescription = description },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = contentColor
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        elevation = ButtonDefaults.elevation(4.dp),
        onClick = onClick
    ) {
        Text(
            stringResource(title),
            color = contentColor
        )
    }
}