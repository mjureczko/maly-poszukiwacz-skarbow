package pl.marianjureczko.poszukiwacz.activity.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.theme.PrimaryBackground

/** Kalinowice */
@Composable
fun CustomScreenBody(goToSearching: (String) -> Unit) {
    val viewModel: CustomMainViewModel = viewModel()
    val state = viewModel.state.value
    Column(Modifier.background(PrimaryBackground)) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .background(PrimaryBackground)
                .weight(0.89f)
        ) {
            Text(
                text = App.getResources().getString(R.string.custom_title),
                style = MaterialTheme.typography.h3,
                textAlign = TextAlign.Center
            )
            Text(
                text = state.messages[state.messageIndex].text,
                style = MaterialTheme.typography.h5,
                textAlign = TextAlign.Justify
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                Image(
                    painterResource(state.messages[state.messageIndex].imageId),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            OutlinedButton(
                shape = RoundedCornerShape(50),
                onClick = { viewModel.nextLeadMessage() },
                content = {
                    Image(
                        imageVector = Icons.Rounded.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.background(color = Color.Transparent),
                        colorFilter = ColorFilter.tint(PrimaryBackground)
                    )
                }
            )
        }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(PrimaryBackground)
        )
        LargeButton(R.string.custom_lets_start) {
            // TODO: start searching activity
        }
        AdvertBanner()
    }
}