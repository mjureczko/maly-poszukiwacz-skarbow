package pl.marianjureczko.poszukiwacz.activity.result.n

import android.annotation.SuppressLint
import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavController?,
    resources: Resources,
    onClickOnGuide: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide) },
        content = {
            ResultScreenBody(resources)
        }
    )
}

@Composable
fun ResultScreenBody(resources: Resources) {
    val viewModel: ResultViewModel = hiltViewModel()
    val state = viewModel.state.value
    Column(Modifier.background(SecondaryBackground)) {
        if (state.resultType == ResultType.TREASURE) {
            //TODO
        } else {
            Spacer(
                modifier = Modifier
                    .weight(0.01f)
                    .background(Color.Transparent)
            )
            val text = if (state.resultType == ResultType.NOT_A_TREASURE) {
                resources.getString(R.string.not_a_treasure_msg)
            } else {
                resources.getString(R.string.treasure_already_taken_msg)
            }
            Text(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.akaya_telivigala)),
                color = Color.Gray,
                textAlign = TextAlign.Center,
                text = text
            )
        }
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        AdvertBanner()
    }

}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun ResultDefaultPreview() {
    AppTheme {
        ResultScreenBody(App.getResources())
    }
}