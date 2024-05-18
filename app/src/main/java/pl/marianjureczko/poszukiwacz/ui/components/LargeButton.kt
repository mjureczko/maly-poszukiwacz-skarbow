package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun LargeButton(title: Int, onClick: () -> Unit) {
    OutlinedButton(
        shape = CutCornerShape(percent = 25),
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        elevation = ButtonDefaults.elevation(4.dp),
        onClick = onClick
    ) {
        Text(
            stringResource(title),
            color = Color.Black
        )
    }
}