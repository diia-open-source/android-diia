package ua.gov.diia.ui_base.components.molecule

import android.media.metrics.PlaybackStateEvent
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.util.MimeTypes
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.noRippleClickable

@Composable
fun FullScreenVideoMlc(
    modifier: Modifier = Modifier, data: FullScreenVideoMlcData, connectivityState: State<Boolean>
) {

    val context = LocalContext.current
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    val mediaItem = MediaItem.Builder()
        .setUri(data.url)
        .setMimeType(MimeTypes.VIDEO_MP4)
        .build()
    exoPlayer.setMediaItem(mediaItem)
    exoPlayer.prepare()

    var lifecycle by remember {
        mutableStateOf(Lifecycle.Event.ON_CREATE)
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            exoPlayer.release()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var shouldShowControls by remember { mutableStateOf(true) }

    var isPlaying by remember { mutableStateOf(exoPlayer.isPlaying) }

    var playbackState by remember { mutableStateOf(exoPlayer.playbackState) }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        DisposableEffect(key1 = Unit) {
            val listener =
                object : Player.Listener {
                    override fun onEvents(
                        player: Player,
                        events: Player.Events
                    ) {
                        super.onEvents(player, events)
                        isPlaying = player.isPlaying
                        playbackState = player.playbackState
                        if (playbackState == STATE_ENDED && shouldShowControls.not()) {
                            shouldShowControls = true
                        }
                    }
                }

            exoPlayer.addListener(listener)

            onDispose {
                exoPlayer.removeListener(listener)
                exoPlayer.release()
            }
        }

        AndroidView(
            modifier = Modifier
                .clipToBounds()
                .noRippleClickable { shouldShowControls = shouldShowControls.not() },
            factory = {
                StyledPlayerView(context).apply {
                    resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                    player = exoPlayer
                    useController = false
                    layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_STOP -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    else -> Unit
                }
            }
        )

        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            isPlaying = { isPlaying },
            playbackState = { playbackState },
            onReplayClick = {
                shouldShowControls = shouldShowControls.not()
                exoPlayer.seekTo(0) },
            onPauseToggle = {
                when {
                    exoPlayer.isPlaying -> {
                        exoPlayer.pause()
                    }

                    playbackState == PlaybackStateEvent.STATE_ENDED -> {
                        exoPlayer.seekTo(0)
                        exoPlayer.play()
                    }

                    else -> {
                        exoPlayer.play()
                        shouldShowControls = false
                    }
                }
                isPlaying = isPlaying.not()
            },
        )
        if (!connectivityState.value) {
            FullScreenLoadingMolecule()
        }
    }
}

@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    onPauseToggle: () -> Unit,
    onReplayClick: () -> Unit,
    playbackState: () -> Int,
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier) {
            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                isPlaying = isPlaying,
                onPauseToggle = onPauseToggle,
                playbackState = playbackState,
                onReplayClick = onReplayClick,
            )
        }
    }
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    playbackState: () -> Int,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
    val playerState = remember(playbackState()) { playbackState() }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(
            modifier = Modifier
                .size(64.dp)
                .noRippleClickable {
                    if (playerState == STATE_ENDED) onReplayClick() else onPauseToggle()
                },
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = when {
                    isVideoPlaying -> {
                        painterResource(id = R.drawable.ic_player_btn_atm_pause)
                    }

                    isVideoPlaying.not() && playerState == STATE_ENDED -> {
                        // Hide the play button when the playback state is ENDED
                        painterResource(id = R.drawable.ic_player_btn_atm_retry)
                    }

                    else -> {
                        painterResource(id = R.drawable.ic_player_btn_atm_play)
                    }
                },
                contentDescription = "Play/Pause"
            )
        }
    }

}


data class FullScreenVideoMlcData(val url: String? = null) : UIElementData

@Preview
@Composable
fun FullScreenVideoMlcPreview() {
    val state = remember { mutableStateOf(true) }
    FullScreenVideoMlc(
        modifier = Modifier,
        data = FullScreenVideoMlcData(url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
        connectivityState = state
    )
}