package pl.marianjureczko.poszukiwacz.ui.theme

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = LightColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

private val LightColorPalette = lightColors(
    primary = PrimaryBackground,
    primaryVariant = Purple700,
    secondary = Teal200,
    background = PrimaryBackground

    /* Other default colors to override
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)