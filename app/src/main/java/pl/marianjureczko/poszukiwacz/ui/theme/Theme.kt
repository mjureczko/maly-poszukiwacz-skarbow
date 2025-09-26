package pl.marianjureczko.poszukiwacz.ui.theme

//import androidx.compose.material3.lightColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        //TODO t:
//        colors = lightColors().copy(
//            primary = colorResource(R.color.colorPrimary)
//        ),
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
