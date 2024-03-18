package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pl.marianjureczko.poszukiwacz.App

@Composable
fun ComposableToast(message: String, duration: Int = 10) {
    val context = App.getAppContext()
    var visible by remember { mutableStateOf(false) }

    if (visible) {
        Snackbar(
//            modifier = Modifier.padding(16.dp),
            content = { Text(message) }
//            duration = duration
        )
    }

    LaunchedEffect(true) {
        visible = true
    }
}
