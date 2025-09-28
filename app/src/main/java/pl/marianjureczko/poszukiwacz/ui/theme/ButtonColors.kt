package pl.marianjureczko.poszukiwacz.ui.theme

import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun buttonColors(): ButtonColors = ButtonDefaults.buttonColors(
    containerColor = Color.LightGray,
    contentColor = Color.Black
)