package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun EmbeddedButton(imageVector: ImageVector, colorFilter: ColorFilter? = null, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier.width(35.dp),
        content = {
            Image(
                imageVector = imageVector,
                contentDescription = null,
                modifier = Modifier.background(color = Color.Transparent),
                colorFilter = colorFilter
            )
        }
    )
}