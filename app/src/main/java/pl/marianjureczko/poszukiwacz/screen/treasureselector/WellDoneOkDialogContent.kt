package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.Link
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes
import pl.marianjureczko.poszukiwacz.ui.theme.buttonColors

private class DummyProvider : PreviewParameterProvider<() -> Unit> {
    override val values = sequenceOf({})
}

@Preview(showBackground = true, apiLevel = 35, backgroundColor = 0xFFFFFFFF)
@Composable
fun WellDoneOkDialogContent(
    @PreviewParameter(DummyProvider::class)
    onClickOnFacebook: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Image(
                painterResource(R.drawable.tada),
                contentDescription = "tada icon",
                modifier = Modifier
                    .padding(end = 5.dp, top = 0.dp)
                    .height(55.dp)
            )
            WellDoneOkDialogText(R.string.well_done)
        }
        WellDoneOkDialogText(R.string.well_done_facebook)
        Button(
            onClick = { onClickOnFacebook() },
            shape = Shapes.large,
            colors = buttonColors(),
        ) {
            Text("Facebook")
            Image(
                painterResource(R.drawable.facebook),
                contentDescription = "Facebook icon",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        WellDoneOkDialogText(R.string.well_done_more)
        MoreApps(moreApsData())
    }
}

@Composable
fun WellDoneOkDialogText(textResourceId: Int) {
    Text(
        text = stringResource(textResourceId),
        style = MaterialTheme.typography.headlineSmall,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun MoreApps(data: List<MoreApsEntry>) {
    Column() {
        data.forEach {
            Row() {
                WellDoneOkDialogText(it.prefixResourceId)
                Spacer(modifier = Modifier.width(10.dp))
                Link(
                    stringResource(it.linkTextResourceId),
                    stringResource(it.linkUrlResourceId)
                )
            }
        }
    }
}

data class MoreApsEntry(
    val prefixResourceId: Int,
    val linkTextResourceId: Int,
    val linkUrlResourceId: Int,
)
