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
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowForward
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
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
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

const val GUIDE_TEXT = "Guide text"
const val GUIDE_IMAGE = "Guide image"
const val NEXT_GUIDE_BUTTON = "Next guide button"
const val PREV_GUIDE_BUTTON = "Previous guide button"
const val START_BUTTON = "Start button"

@Composable
fun MainScreenBody(modifier: Modifier, goToSearching: GoToSearching) {
    val viewModel: MainViewModel = hiltViewModel()
    val state = viewModel.state.value
    LaunchedEffect(key1 = "on start") {
        viewModel.initializeAssets()
    }
    Column(modifier.background(colorResource(R.color.colorBackgroundVariant))) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .weight(0.89f)
        ) {
            Text(
                text = stringResource(R.string.custom_title),
                style = MaterialTheme.typography.displaySmall,
                color = colorResource(R.color.colorPrimaryVariant),
                textAlign = TextAlign.Center
            )
            val style = if (1.0.dh.value > 850) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.bodyLarge
            }
            Text(
                text = state.messages[state.messageIndex].text,
                style = style,
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
            if (!state.isFirstMessage()) {
                PrevButton(viewModel)
                Spacer(modifier = Modifier.width(8.dp))
            }
            if (!state.isLastMessage()) {
                NextButton(viewModel)
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }
        }
        Spacer(modifier = Modifier.weight(0.01f))
        LargeButton(R.string.custom_lets_start, START_BUTTON, enabled = state.assetsCopied) {
            viewModel.restartMessages()
            goToSearching.invoke(CustomInitializerForRoute.routeName)
        }
        AdvertBanner()
    }
}

@Composable
private fun NextButton(viewModel: MainViewModel) {
    ArrowButton(NEXT_GUIDE_BUTTON, Icons.Rounded.ArrowForward) { viewModel.nextLeadMessage() }
}

@Composable
private fun PrevButton(viewModel: MainViewModel) {
    ArrowButton(PREV_GUIDE_BUTTON, Icons.Rounded.ArrowBack) { viewModel.prevLeadMessage() }
}

@Composable
private fun ArrowButton(description: String, arrowIcon: ImageVector, onClickAction: () -> Unit) {
    OutlinedButton(
        shape = Shapes.small,
        onClick = onClickAction,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.LightGray),
        modifier = Modifier.semantics { contentDescription = description },
        content = {
            Image(
                imageVector = arrowIcon,
                contentDescription = null,
                modifier = Modifier.background(color = Color.Transparent),
                colorFilter = ColorFilter.tint(Color.Black)
            )
        }
    )
}