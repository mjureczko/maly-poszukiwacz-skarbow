package pl.marianjureczko.poszukiwacz.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import pl.marianjureczko.poszukiwacz.R

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = lightColors().copy(
            primary = colorResource(R.color.colorPrimary)
        ),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
