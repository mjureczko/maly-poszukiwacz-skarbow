package pl.marianjureczko.poszukiwacz.activity.searching

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.window.layout.WindowMetricsCalculator
import pl.marianjureczko.poszukiwacz.App
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.main.MainScreen
import pl.marianjureczko.poszukiwacz.ui.Screen.dh
import pl.marianjureczko.poszukiwacz.ui.Screen.dw
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.theme.AppTheme
import pl.marianjureczko.poszukiwacz.ui.theme.PrimaryBackground
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SearchingScreen(onClickOnGuide: () -> Unit) {
    Scaffold(
        topBar = { TopBar(onClickOnGuide) },
        content = {
            Column(Modifier.background(SecondaryBackground)) {
                Scores()
                Compass()
                Steps()
                Buttons()
                Spacer(
                    modifier = Modifier
                        .weight(0.01f)
                        .background(Color.Transparent)
                )
                AdvertBanner()
            }
        }
    )
}

@Composable
fun Scores() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.05.dh),
    ) {
        Text(color = Color.Black, text = "TODO")
    }
}

@Composable
fun Compass() {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .height(0.35.dh),
        contentAlignment = Alignment.Center
    ) {
//Text("My minHeight is $minHeight while my maxWidth is $maxWidth")
        Image(
            painterResource(R.drawable.compass),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
        Image(
            painterResource(R.drawable.arrow),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
fun Steps() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.15.dh),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.padding(start = 50.dp), fontSize = 56.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Gray,
            text = "99"
        )
        Image(
            painterResource(R.drawable.steps),
            modifier = Modifier.padding(start = 50.dp),
            contentDescription = null,
            contentScale = ContentScale.Inside,
        )
    }
}

@Composable
fun Buttons() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .height(0.20.dh),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.width(0.2.dw)) {
            Image(
                painterResource(R.drawable.map),
                modifier = Modifier.padding(10.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
            Image(
                painterResource(R.drawable.change_chest),
                modifier = Modifier.padding(10.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
        }
        Column(modifier = Modifier.width(0.6.dw)) {
            Image(
                painterResource(R.drawable.chest),
                modifier = Modifier.padding(start = 20.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
        }
        Column(modifier = Modifier.width(0.2.dw)) {
            Image(
                painterResource(R.drawable.show_photo),
                modifier = Modifier.padding(10.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
            Image(
                painterResource(R.drawable.megaphone),
                modifier = Modifier.padding(10.dp),
                contentDescription = null,
                contentScale = ContentScale.Inside,
            )
        }
    }
}

@Preview(showBackground = true, apiLevel = 31)
@Composable
fun DefaultPreview() {
    AppTheme {
        SearchingScreen({})
    }
}