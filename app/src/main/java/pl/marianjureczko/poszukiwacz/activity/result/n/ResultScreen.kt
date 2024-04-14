package pl.marianjureczko.poszukiwacz.activity.result.n

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import pl.marianjureczko.poszukiwacz.R
import pl.marianjureczko.poszukiwacz.activity.searching.n.ResultSharedViewModel
import pl.marianjureczko.poszukiwacz.activity.searching.n.SharedViewModel
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.shareViewModelStoreOwner
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT
import pl.marianjureczko.poszukiwacz.ui.theme.SecondaryBackground

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide) },
        content = {
            ResultScreenBody(shareViewModelStoreOwner(navBackStackEntry, navController))
        }
    )
}

@Composable
fun ResultScreenBody(viewModelStoreOwner: NavBackStackEntry) {
    val localViewModel: ResultViewModel = hiltViewModel()
    val localState = localViewModel.state.value
    val sharedViewModel: ResultSharedViewModel = getViewModel(viewModelStoreOwner)
    sharedViewModel.resultPresented()
    val snackbarCoroutineScope = rememberCoroutineScope()
    Column(Modifier.background(SecondaryBackground)) {
        if (localState.resultType == ResultType.TREASURE) {
            //TODO
        } else {
            Spacer(
                modifier = Modifier
                    .weight(0.01f)
                    .background(Color.Transparent)
            )
            val text = if (localState.resultType == ResultType.NOT_A_TREASURE) {
                stringResource(R.string.not_a_treasure_msg)
            } else {
                stringResource(R.string.treasure_already_taken_msg)
            }
            Text(
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FANCY_FONT,
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

@Composable
private fun getViewModel(viewModelStoreOwner: NavBackStackEntry): ResultSharedViewModel {
    val viewModelDoNotInline: SharedViewModel = hiltViewModel(viewModelStoreOwner)
    return viewModelDoNotInline
}

//@Preview(showBackground = true, apiLevel = 31)
//@Composable
//fun ResultDefaultPreview() {
//    AppTheme {
//        ResultScreenBody(App.getResources())
//    }
//}