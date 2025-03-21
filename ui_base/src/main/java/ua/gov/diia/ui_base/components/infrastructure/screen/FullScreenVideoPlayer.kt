package ua.gov.diia.ui_base.components.infrastructure.screen

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.delay
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.media.player.ExoPlayerVM
import java.util.UUID

@Composable
fun FullScreenVideoPlayer(
    modifier: Modifier = Modifier,
    url: String,
    clickable: Boolean = true,
    connectivityState: Boolean
) {
    val id = remember { UUID.randomUUID().toString() }
    val vm: ExoPlayerVM = hiltViewModel(key = id)
    var lifecycle by remember { mutableStateOf(Lifecycle.Event.ON_CREATE) }
    var shouldShowControls by remember { mutableStateOf(true) }
    var isPlaying by remember { mutableStateOf(vm.player.isPlaying) }
    var playbackState by remember { mutableStateOf(vm.player.playbackState) }
    val lifecycleOwner = LocalLifecycleOwner.current

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
            }
        }

        vm.player.addListener(listener)

        onDispose {
            vm.player.removeListener(listener)
        }
    }

    LaunchedEffect(key1 = url) {
        url.let {
            vm.addVideoUri(url)
        }
    }

    LaunchedEffect(progress) {
        shouldShowControls = progress <= 3000L || playbackState == Player.STATE_ENDED
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        AndroidView(
            modifier = Modifier
                .conditional(clickable) {
                    clickable { shouldShowControls = shouldShowControls.not() }
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

        if (!connectivityState) {
            FullScreenLoadingMolecule(backgroundColor = Color.Transparent)
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
                    onReplayClick = onReplayClick
                )

            }
            PlayerNavMlc(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
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
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
    val playerState = remember(playbackState()) { playbackState() }

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
