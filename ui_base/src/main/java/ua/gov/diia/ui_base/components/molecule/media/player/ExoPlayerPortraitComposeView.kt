package ua.gov.diia.ui_base.components.molecule.media.player

import android.media.metrics.PlaybackStateEvent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.theme.BlueAlpha37
import java.util.UUID

@Composable
fun ExoPlayerPortraitComposeView(
    modifier: Modifier = Modifier,
    url: String?,
    inCarousel: Boolean = false,
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
    var isBuffering by remember { mutableStateOf(true) }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            lifecycle = event
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
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
                    isBuffering = playbackState == Player.STATE_BUFFERING
                }
            }

        vm.player.addListener(listener)

        onDispose {
            vm.player.removeListener(listener)
        }
    }
    LaunchedEffect(key1 = url) {
        url?.let {
            vm.addVideoUri(url)
        }
    }

    Box(
        modifier = modifier
            .conditional(!inCarousel) {
                padding(horizontal = 24.dp)
                    .padding(top = 24.dp)
            }
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .clip(RoundedCornerShape(16.dp))
    ) {
        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    player = vm.player
                    useController = false
                }
            },
            update = {
                when (lifecycle) {
                    Lifecycle.Event.ON_PAUSE -> {
                        it.onPause()
                        it.player?.pause()
                    }

                    Lifecycle.Event.ON_RESUME -> {
                        it.onResume()
                    }

                    else -> Unit
                }
            },
            modifier = Modifier.conditional(clickable) {
                clickable {
                    shouldShowControls = shouldShowControls.not()
                }
            }
        )
        PlayerControls(
            modifier = Modifier.fillMaxSize(),
            isVisible = { shouldShowControls },
            isPlaying = { isPlaying },
            isBuffering = { isBuffering },
            playbackState = { playbackState },
            onReplayClick = {
                vm.player.seekTo(0)
                vm.player.play()
            },
            onPauseToggle = {
                when {
                    vm.player.isPlaying -> {
                        vm.player.pause()
                    }

                    playbackState == PlaybackStateEvent.STATE_ENDED -> {
                        vm.player.seekTo(0)
                        vm.player.play()
                    }

                    else -> {
                        vm.player.play()
                    }
                }
                isPlaying = isPlaying.not()
            }
        )
        if (!connectivityState ||  isBuffering) {
            FullScreenLoadingMolecule()
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
) {

    val visible = remember(isVisible()) { isVisible() }

    AnimatedVisibility(
        modifier = modifier,
        visible = visible,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(modifier = Modifier.background(BlueAlpha37)) {
            CenterControls(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                isPlaying = isPlaying,
                onPauseToggle = onPauseToggle,
                playbackState = playbackState,
                onReplayClick = onReplayClick,
                isBuffering = isBuffering
            )
        }
    }
}

@Composable
private fun CenterControls(
    modifier: Modifier = Modifier,
    isPlaying: () -> Boolean,
    isBuffering: () -> Boolean,
    playbackState: () -> Int,
    onReplayClick: () -> Unit,
    onPauseToggle: () -> Unit,
) {
    val isVideoPlaying = remember(isPlaying()) { isPlaying() }
    val playerState = remember(playbackState()) { playbackState() }
    val buffering = remember(isBuffering()) { isBuffering() }
    if (buffering) return

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        Box(
            modifier = Modifier
                .size(40.dp)
                .clickable { if (playerState == PlaybackStateEvent.STATE_ENDED) onReplayClick() else onPauseToggle() },
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = when {
                    isVideoPlaying -> {
                        painterResource(id = R.drawable.ic_player_pause)
                    }

                    isVideoPlaying.not() && playerState == PlaybackStateEvent.STATE_ENDED -> {
                        // Hide the play button when the playback state is ENDED
                        painterResource(id = R.drawable.ic_player_play)
                    }

                    else -> {
                        painterResource(id = R.drawable.ic_player_play)
                    }
                },
                contentDescription = "Play/Pause"
            )
        }
    }

}