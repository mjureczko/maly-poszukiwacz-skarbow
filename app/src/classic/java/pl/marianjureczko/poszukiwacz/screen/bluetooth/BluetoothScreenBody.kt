package pl.marianjureczko.poszukiwacz.screen.bluetooth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton

@Composable
fun BluetoothScreenBody(
    modifier: Modifier,
    state: BluetoothState,
    onDeviceSelected: OnDeviceSelected,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Bottom
    ) {
        if (state.shouldShowDeviceSelection()) {
            DeviceSelection(devices = state.devices, onDeviceSelected = onDeviceSelected)
        }

        Console(Modifier.weight(1f), state.messages)

        val isInPreview = LocalInspectionMode.current
        if (!isInPreview) {
            AdvertBanner()
        }
    }
}

@Composable
fun DeviceSelection(
    modifier: Modifier = Modifier,
    devices: List<String>,
    onDeviceSelected: OnDeviceSelected
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        val selectDeviceMsg = stringResource(R.string.select_bluetooth_device)
        Message(listOf(selectDeviceMsg), 0)
        LazyColumn {
            items(devices.size) { index ->
                LargeButton(devices[index]) { onDeviceSelected.sentRouteToDevice(devices[index]) }
            }
        }
    }
}

@Composable
private fun Console(modifier: Modifier, messages: List<String>) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
        reverseLayout = true
    ) {
        items(messages.size) { index ->
            Message(messages, index)
        }
    }
}

@Composable
private fun Message(messages: List<String>, index: Int) {
    Text(
        modifier = Modifier.padding(2.dp),
        color = Color(0xFF00FF00),
        fontSize = 24.sp,
        fontFamily = FontFamily.Monospace,
        text = "> ${messages[messages.size - index - 1]}"
    )
}