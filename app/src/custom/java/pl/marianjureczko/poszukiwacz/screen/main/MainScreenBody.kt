package pl.marianjureczko.poszukiwacz.screen.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

const val GUIDE_TEXT = "Guide text"
const val GUIDE_IMAGE = "Guide image"
const val NEXT_GUIDE_BUTTON = "Next guide button"
const val START_BUTTON = "Start button"

@Composable
fun MainScreenBody(goToSearching: GoToSearching) {
    val viewModel: MainViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column(Modifier.background(colorResource(R.color.colorBackgroundVariant))) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .weight(0.89f)
        ) {
            Text(
                text = stringResource(R.string.custom_title),
                style = MaterialTheme.typography.h3,
                color = colorResource(R.color.colorPrimaryVariant),
                textAlign = TextAlign.Center
            )
            Text(
                text = state.messages[state.messageIndex].text,
                color = colorResource(R.color.colorPrimaryVariant),
                textAlign = TextAlign.Justify,
                modifier = Modifier.semantics { contentDescription = GUIDE_TEXT }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .height(150.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                val imageId = state.messages[state.messageIndex].imageId
                Image(
                    painterResource(imageId),
                    contentDescription = GUIDE_IMAGE,
                    contentScale = ContentScale.Inside,
                    modifier = Modifier.testTag(imageId.toString())
                )
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.End,
        ) {
            if (!state.isLastMessage()) {
                NextButton(viewModel)
            }
        }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
        )
        LargeButton(R.string.custom_lets_start, START_BUTTON) {
            viewModel.restartMessages()
            goToSearching.invoke(CustomInitializerForRoute.routeName)
        }
        AdvertBanner()
    }
}

@Composable
private fun NextButton(viewModel: MainViewModel) {
    OutlinedButton(
        shape = Shapes.small,
        onClick = { viewModel.nextLeadMessage() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.LightGray), // Border color and thickness
        elevation = ButtonDefaults.elevation(4.dp),
        modifier = Modifier.semantics { contentDescription = NEXT_GUIDE_BUTTON },
        content = {
            Image(
                imageVector = Icons.Rounded.ArrowForward,
                contentDescription = null,
                modifier = Modifier.background(color = Color.Transparent),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }
    )
}