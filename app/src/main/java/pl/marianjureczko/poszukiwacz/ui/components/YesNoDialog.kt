package pl.marianjureczko.poszukiwacz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.marianjureczko.poszukiwacz.R

const val YES_BUTTON = "Yes"
const val NO_BUTTON = "Nope"

@Composable
fun YesNoDialog(
    state: Boolean,
    hideIt: () -> Unit,
    title: Int? = null,
    titleString: String? = null,
    onYes: () -> Unit
) {
    AbstractDialog(state, hideIt, title, titleString,
        buttons = {
                Row(
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            onYes()
                            hideIt()
                        }) {
                        Text(
                            modifier = Modifier.semantics { contentDescription =  YES_BUTTON},
                            text = stringResource(R.string.yes)
                        )
                    }
                    TextButton(
                        modifier = Modifier.semantics { contentDescription =  NO_BUTTON },
                        onClick = { hideIt() }
                    ) {
                        Text(stringResource(R.string.no))
                    }
                }
        }
    )
}

@Preview(apiLevel = 31)
@Composable
fun YesNoDialogPreview() {
    YesNoDialog(true, {}, null, "title", {})
}