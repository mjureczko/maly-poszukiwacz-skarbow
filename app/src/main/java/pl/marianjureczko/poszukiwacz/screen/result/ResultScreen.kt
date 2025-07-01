package pl.marianjureczko.poszukiwacz.screen.result

import android.annotation.SuppressLint
import android.media.MediaPlayer
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
import pl.marianjureczko.poszukiwacz.model.TreasureType
import pl.marianjureczko.poszukiwacz.screen.searching.ResultSharedViewModel
import pl.marianjureczko.poszukiwacz.screen.searching.SharedViewModel
import pl.marianjureczko.poszukiwacz.shared.GoToFacebook
import pl.marianjureczko.poszukiwacz.shared.GoToGuide
import pl.marianjureczko.poszukiwacz.shared.UpdateSubtitlesLine
import pl.marianjureczko.poszukiwacz.ui.components.AdvertBanner
import pl.marianjureczko.poszukiwacz.ui.components.MenuConfig
import pl.marianjureczko.poszukiwacz.ui.components.TopBar
import pl.marianjureczko.poszukiwacz.ui.shareViewModelStoreOwner
import pl.marianjureczko.poszukiwacz.ui.theme.FANCY_FONT

const val PLAY_MOVIE_BUTTON = "Play the movie"
const val TREASURE_QUANTITY = "treasure quantity"
const val DO_NOT_SHOW_TREASURE_MSG = "Cannot show treasure"
private const val SUBTITLES_MIME_TYPE = "application/x-subrip"

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ResultScreen(
    navController: NavController,
    navBackStackEntry: NavBackStackEntry,
    onClickOnGuide: GoToGuide,
    onClickOnFacebook: GoToFacebook
) {
    val sharedViewModel: ResultSharedViewModel =
        getViewModel(shareViewModelStoreOwner(navBackStackEntry, navController))
    Scaffold(
        topBar = {
            TopBar(
                navController = navController,
                title = stringResource(R.string.treasure),
                menuConfig = MenuConfig(onClickOnGuide, { onClickOnFacebook(sharedViewModel.getRouteName()) }),
            )
        },
        content = { ResultScreenBody(sharedViewModel) }
    )
}

@Composable
fun ResultScreenBody(sharedViewModel: ResultSharedViewModel) {
    val localViewModel: ResultViewModel = hiltViewModel()
    val localState: ResultState = localViewModel.state.value
    sharedViewModel.resultPresented()
    Column {
        Spacer(
            modifier = Modifier
                .weight(0.01f)
                .background(Color.Transparent)
        )
        if (localState.resultType == ResultType.KNOWLEDGE && localState.moviePath != null) {
            VideoPlayerWithButton(
                localState.isPlayVisible,
                localViewModel,
                localState.moviePath,
                localState.subtitlesLine,
                localState.subtitlesPath,
                localState.localesWithSubtitles
            ) { localViewModel.setSubtitlesLine(it) }
        } else if (localState.resultType in setOf(ResultType.GOLD, ResultType.RUBY, ResultType.DIAMOND)) {
            TreasureImage(localState.treasureType, localState.amount!!)
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

/**
 * @param resultType the type should be one of: GOLD, RUBY, DIAMOND.
 */
@Composable
@Preview(showBackground = true, apiLevel = 31)
private fun TreasureImage(treasureType: TreasureType? = TreasureType.GOLD, amount: Int = 91) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        treasureType?.let {
            val treasureImage = when (it) {
                TreasureType.GOLD -> R.drawable.gold
                TreasureType.RUBY -> R.drawable.ruby
                TreasureType.DIAMOND -> R.drawable.diamond
                TreasureType.KNOWLEDGE -> throw IllegalArgumentException("Show movie for knowledge")
            }
            Image(
                painterResource(treasureImage),
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .testTag(treasureImage.toString()),
                contentDescription = "${treasureType.name} treasure",
                contentScale = ContentScale.Inside,
            )
            Text(
                modifier = Modifier.semantics { contentDescription = TREASURE_QUANTITY },
                fontSize = 60.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FANCY_FONT,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                text = "$amount",
            )
        }
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
        modifier = Modifier.semantics { contentDescription = DO_NOT_SHOW_TREASURE_MSG },
        fontSize = 60.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = FANCY_FONT,
        color = Color.Gray,
        textAlign = TextAlign.Center,
        text = text,
    )
}

@Composable
@Preview(showBackground = true, apiLevel = 31)
private fun VideoPlayerWithButton(
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
    updateSubtitlesLine: UpdateSubtitlesLine = {}
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
            videoView.setOnPreparedListener { mediaPlayer ->
                if (localesWithSubtitles) {
                    registerSubtitles(subtitlesPath, mediaPlayer, updateSubtitlesLine)
                }
            }
            Video(videoView, movieController, moviePath)
            PlayButton(isPlayVisible, videoView, movieController)
            if (localesWithSubtitles) {
                SubtitlesLine(subtitlesLine, Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}

@Composable
private fun SubtitlesLine(subtitlesLine: String?, modifier: Modifier) {
    subtitlesLine?.let {
        Text(
            fontSize = 45.sp,
            fontFamily = FANCY_FONT,
            color = Color.White,
            textAlign = TextAlign.Center,
            text = it,
            modifier = modifier,
            style = TextStyle(shadow = Shadow(color = Color.Black, blurRadius = 12f))
        )
    }
}

private fun registerSubtitles(
    subtitlesPath: String?,
    mediaPlayer: MediaPlayer,
    updateSubtitlesLine: UpdateSubtitlesLine
) {
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

@Composable
private fun Video(
    videoView: VideoView,
    movieController: MovieController,
    moviePath: String
) {
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
            contentDescription = PLAY_MOVIE_BUTTON,
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
