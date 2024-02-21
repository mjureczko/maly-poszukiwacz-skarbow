package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.marianjureczko.poszukiwacz.App

@Composable
fun LargeButton(title: Int, onClick: () -> Unit) {
    OutlinedButton(
        shape = CutCornerShape(percent = 25),
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Text(App.getResources().getString(title))
    }
}