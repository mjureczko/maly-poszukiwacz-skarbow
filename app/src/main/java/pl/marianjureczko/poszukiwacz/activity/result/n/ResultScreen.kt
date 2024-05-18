package pl.marianjureczko.poszukiwacz.activity.result.n

import android.annotation.SuppressLint
import android.media.MediaPlayer.TrackInfo
import android.widget.VideoView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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

private const val SUBTITLES_MIME_TYPE = "application/x-subrip"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: () -> Unit,
    onClickOnFacebook: () -> Unit
) {
    Scaffold(
        topBar = { TopBar(navController, onClickOnGuide, onClickOnFacebook) },
        content = {
            ResultScreenBody(shareViewModelStoreOwner(navBackStackEntry, navController))
        }
    )
}

@Composable
fun ResultScreenBody(viewModelStoreOwner: NavBackStackEntry) {
    val localViewModel: ResultViewModel = hiltViewModel()
    val localState: ResultState = localViewModel.state.value
    val sharedViewModel: ResultSharedViewModel = getViewModel(viewModelStoreOwner)
    sharedViewModel.resultPresented()
    val snackbarCoroutineScope = rememberCoroutineScope()
    Column(Modifier.background(SecondaryBackground)) {
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        if (localState.resultType == ResultType.TREASURE && localState.moviePath != null) {
            Movie(
                localState.isPlayVisible,
                localViewModel,
                localState.moviePath,
                localState.subtitlesLine,
                localState.subtitlesPath,
                localState.localesWithSubtitles
            ) { localViewModel.setSubtitlesLine(it) }
        } else {
            Message(localState)
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
private fun Message(localState: ResultState) {
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

@Composable
@Preview(showBackground = true, apiLevel = 31)
private fun Movie(
    isPlayVisible: Boolean = true,
    movieController: MovieController = object : MovieController {
        override fun onPlay() {}
        override fun onPause() {}
        override fun onMovieFinished() {}
    },
    moviePath: String = "/data/user/0/pl.marianjureczko.poszukiwacz.kalinowice/files/treasures_lists/kalinowice_01.mp4",
    subtitlesLine: String? = null,
    subtitlesPath: String? = null,
    localesWithSubtitles: Boolean = false,
    updateSubtitlesLine: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxHeight(0.95F)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.9f),
            contentAlignment = Alignment.Center
        ) {
            val context = LocalContext.current
            val videoView: VideoView = remember { VideoView(context) }
            Video(videoView, subtitlesPath, updateSubtitlesLine, movieController, moviePath, localesWithSubtitles)
            PlayButton(isPlayVisible, videoView, movieController)
            //TODO t: merge conditions, and maybe move to  state
            if (localesWithSubtitles) {
                subtitlesLine?.let {
                    Text(
                        fontSize = 45.sp,
                        fontFamily = FANCY_FONT,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        text = it,
                        modifier = Modifier.align(Alignment.BottomCenter),
                        style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 12f))
                    )
                }
            }
        }
    }
}

@Composable
private fun Video(
    videoView: VideoView,
    subtitlesPath: String?,
    updateSubtitlesLine: (String) -> Unit,
    movieController: MovieController,
    moviePath: String,
    localesWithSubtitles: Boolean
) {
    videoView.setOnPreparedListener { mediaPlayer ->
        //TODO t: merge conditions, and maybe move to  state
        if (localesWithSubtitles) {
            subtitlesPath?.let {
                mediaPlayer.addTimedTextSource(subtitlesPath, SUBTITLES_MIME_TYPE)
                val textTrackIndex: Int = findTrackIndexFor(
                    TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, mediaPlayer.getTrackInfo()
                )
                if (textTrackIndex >= 0) {
                    mediaPlayer.selectTrack(textTrackIndex)
                }
                mediaPlayer.setOnTimedTextListener { _, text -> updateSubtitlesLine(text.text) }
            }
        }
    }
    AndroidView(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .clickable {
                videoView.pause()
                movieController.onPause()
            },
        factory = { _ ->
            videoView.apply {
                setVideoPath(moviePath)
                setOnCompletionListener {
                    movieController.onMovieFinished()
                    videoView.seekTo(0)
                }
                videoView.seekTo(1)
            }
        })
}

@Composable
private fun PlayButton(
    isPlayVisible: Boolean,
    videoView: VideoView,
    movieController: MovieController
) {
    if (isPlayVisible) {
        Image(
            painterResource(R.drawable.play),
            modifier = Modifier
                .padding(10.dp)
                .clickable {
                    videoView.start()
                    movieController.onPlay()
                },
            contentDescription = "Play the movie",
            contentScale = ContentScale.Inside,
        )
    }
}

private fun findTrackIndexFor(mediaTrackType: Int, trackInfo: Array<TrackInfo>): Int {
    val index = -1
    for (i in trackInfo.indices) {
        if (trackInfo[i].trackType == mediaTrackType) {
            return i
        }
    }
    return index
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