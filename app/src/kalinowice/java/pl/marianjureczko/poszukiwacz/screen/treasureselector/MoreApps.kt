package pl.marianjureczko.poszukiwacz.screen.treasureselector

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.Link

@Composable
fun MoreApps() {
    Column() {
        OkDialogText(R.string.well_done_classic_app)
        Link(
            stringResource(R.string.well_done_little_treasure_hunter),
            "https://play.google.com/store/apps/details?id=pl.marianjureczko.poszukiwacz"
        )
    }
}
