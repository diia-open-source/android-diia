package ua.gov.diia.ui_base.components.molecule

import android.content.Context
import android.media.AudioManager
import androidx.annotation.OptIn
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.screen.PlayerNavMlc
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.media.player.ExoPlayerVM
import java.util.UUID

@OptIn(UnstableApi::class)
@Composable
fun FullScreenVideoMlc(
    modifier: Modifier = Modifier,
    data: FullScreenVideoMlcData,
    connectivityState: Boolean
) {
    val id = remember { UUID.randomUUID().toString() }
    val vm: ExoPlayerVM = hiltViewModel(key = id)
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    var shouldShowControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(vm.player.isPlaying) }
    var playbackState by remember { mutableStateOf(vm.player.playbackState) }
    val lifecycleOwner = LocalLifecycleOwner.current
    var isBuffering by remember { mutableStateOf(true) }

    val audioManager = LocalContext.current.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val isSilent = audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT ||
            audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE

    LaunchedEffect(isSilent) {
        if (isSilent) {
            vm.player.volume = 0f
        } else {
            vm.player.volume = 1f
        }
    }


    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    var totalDuration by remember { mutableStateOf(0L) }
    var currentTime by remember { mutableStateOf(0L) }
    var bufferedPercentage by remember { mutableStateOf(0) }
    val progress = vm.progress.collectAsState().value

    DisposableEffect(key1 = Unit) {
        val listener = object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                isPlaying = player.isPlaying
                playbackState = player.playbackState
                totalDuration = player.duration.coerceAtLeast(0L)
                currentTime = player.currentPosition.coerceAtLeast(0L)
                bufferedPercentage = player.bufferedPercentage
                isBuffering = playbackState == Player.STATE_BUFFERING
            }
        }

        vm.player.addListener(listener)

        onDispose {
            vm.player.removeListener(listener)
        }
    }

    LaunchedEffect(key1 = data.url) {
        data.url?.let {
            vm.addVideoUri(it)
        }
    }
    var hideControlsTimeout by remember { mutableStateOf(false) }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            delay(3000)
            hideControlsTimeout = true
            shouldShowControls = false
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(9f / 16f)
    ) {
        AndroidView(
            modifier = Modifier
                .clickable {
                    shouldShowControls = shouldShowControls.not()
                    if (hideControlsTimeout) {
                        hideControlsTimeout = false
                    }
                }
                .align(Alignment.Center),
            factory = { context ->
                PlayerView(context).apply {
                    player = vm.player
                    useController = false
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
            isVisible = { shouldShowControls || !connectivityState },
            isPlaying = { isPlaying },
            playbackState = { playbackState },
            isBuffering = { isBuffering },
            onReplayClick = {
                vm.player.seekTo(0)
            },
            onPauseToggle = {
                when {
                    vm.player.isPlaying -> {
                        vm.player.pause()
                    }

                    playbackState == Player.STATE_ENDED -> {
                        vm.player.seekTo(0)
                        vm.player.play()
                    }

                    else -> {
                        vm.player.play()
                    }
                }
                isPlaying = isPlaying.not()
            },
            totalDuration = { totalDuration },
            currentTime = { progress },
            bufferedPercentage = { bufferedPercentage },
            onSeekChanged = { timeMs: Float ->
                vm.player.seekTo(timeMs.toLong())
            },
            sliderPosition = { currentTime.toFloat() }
        )

        if (!connectivityState || isBuffering) {
            FullScreenLoadingMolecule(backgroundColor = Color.Transparent)
        }
    }
}

@Composable
private fun PlayerControls(
    modifier: Modifier = Modifier,
    isVisible: () -> Boolean,
    isPlaying: () -> Boolean,
    isBuffering: () -> Boolean,
    onPauseToggle: () -> Unit,
    onReplayClick: () -> Unit,
    playbackState: () -> Int,
    totalDuration: () -> Long,
    currentTime: () -> Long,
    bufferedPercentage: () -> Int,
    onSeekChanged: (timeMs: Float) -> Unit,
    sliderPosition: () -> Float
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CenterControls(
                    isPlaying = isPlaying,
                    onPauseToggle = onPauseToggle,
                    playbackState = playbackState,
                    onReplayClick = onReplayClick,
                    isBuffering = isBuffering
                )

            }
            PlayerNavMlc(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 96.dp)
                    .fillMaxWidth()
                    .animateEnterExit(
                        enter = slideInVertically(
                            initialOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        ),
                        exit = slideOutVertically(
                            targetOffsetY = { fullHeight: Int ->
                                fullHeight
                            }
                        )
                    ),
                totalDuration = totalDuration,
                currentTime = currentTime,
                bufferPercentage = bufferedPercentage,
                onSeekChanged = onSeekChanged,
                sliderPosition = sliderPosition
            )
        }
    }
}

@Composable
private fun CenterControls(
    isPlaying: () -> Boolean,
    playbackState: () -> Int,
    isBuffering: () -> Boolean,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
    val playerState = remember(playbackState()) { playbackState() }
    val buffering = remember(isBuffering()) { isBuffering() }
    if (buffering) return
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clickable {
                    if (playerState == Player.STATE_ENDED) {
                        onReplayClick()
                    } else {
                        onPauseToggle()
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (playerState == Player.STATE_ENDED) {
                Image(
                    painter = painterResource(id = R.drawable.ic_player_btn_atm_replay),
                    contentDescription = "Replay"
                )
            } else {
                Image(
                    painter = if (isVideoPlaying) painterResource(id = R.drawable.ic_player_btn_atm_pause)
                    else painterResource(id = R.drawable.ic_player_btn_atm_play),
                    contentDescription = "Play/Pause"
                )
            }
        }
    }
}

data class FullScreenVideoMlcData(
    val url: String? = null
) : UIElementData

@Preview
@Composable
fun FullScreenVideoMlcPreview() {
    val state = remember { mutableStateOf(true) }
    FullScreenVideoMlc(
        modifier = Modifier,
        data = FullScreenVideoMlcData(url = "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerFun.mp4"),
        connectivityState = state.value
    )
}