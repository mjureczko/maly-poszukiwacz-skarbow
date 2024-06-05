package pl.marianjureczko.poszukiwacz.activity.main

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.shared.GoToSearching
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.LargeButton
import pl.marianjureczko.poszukiwacz.ui.theme.Shapes

/** Kalinowice */
@Composable
fun CustomScreenBody(goToSearching: GoToSearching) {
    val viewModel: CustomMainViewModel = hiltViewModel()
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
                textAlign = TextAlign.Justify
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
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
            if (!state.isLastMessage()) {
                NextButton(viewModel)
            }
        }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
        )
        LargeButton(R.string.custom_lets_start) {
            viewModel.restartMessages()
            goToSearching.invoke(CustomInitializerForRoute.routeName)
        }
        AdvertBanner()
    }
}

@Composable
private fun NextButton(viewModel: CustomMainViewModel) {
    OutlinedButton(
        shape = Shapes.small,
        onClick = { viewModel.nextLeadMessage() },
        colors = ButtonDefaults.buttonColors(
            backgroundColor = Color.White,
            contentColor = Color.Black
        ),
        border = BorderStroke(2.dp, Color.LightGray), // Border color and thickness
        elevation = ButtonDefaults.elevation(4.dp),
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