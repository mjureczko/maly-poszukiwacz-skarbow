package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

@Composable
fun MyCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp,
            pressedElevation = 8.dp,
        ),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = Shapes.large,
        modifier = modifier.padding(4.dp),
    ) {
        content()
    }
}