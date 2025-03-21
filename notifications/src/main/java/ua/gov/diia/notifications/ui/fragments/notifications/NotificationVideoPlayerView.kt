package ua.gov.diia.notifications.ui.fragments.notifications

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.annotation.OptIn
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.gov.diia.core.util.extensions.addFlagKeepScreen
import ua.gov.diia.core.util.extensions.clearFlagKeepScreen
import ua.gov.diia.core.util.extensions.lifecycle.lifecycleScope
import ua.gov.diia.notifications.R

class NotificationVideoPlayerView @OptIn(UnstableApi::class)
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr),
    Player.Listener,
    View.OnClickListener {

    private enum class PlaybackViewState {
        BUFFERING, PAUSE, PLAY
    }

    private companion object {
        const val ANIM_DURATION_CONTROL_PANEL_VISIBILITY = 300L
        const val DELAY_HIDE_CONTROL_PANEL = 3000L
    }


    private val playerView: PlayerView
    private val videoDimmer: View
    private val playbackButton: FrameLayout
    private val viewBuffering: ProgressBar
    private val viewPlay: ImageView
    private val viewPause: ImageView

    private var currentPlaybackView: View

    private var hideControlPanelJob: Job? = null

    private var isPlaybackViewVisible = true
        set(visible) {
            field = visible
            setupPlayerActionListeners(visible)
        }

    var player: Player? = null
        set(newPlayer) {
            if (player == newPlayer) return

            this.player?.apply {
                pause()
                removeListener(this@NotificationVideoPlayerView)
            }

            field = newPlayer
            playerView.player = newPlayer
            newPlayer?.addListener(this)
        }

    init {
        inflate(context, R.layout.view_message_video_player, this)

        playerView = findViewById(R.id.player_view_notification)
        videoDimmer = findViewById(R.id.video_dimer)
        playbackButton = findViewById(R.id.exo_custom_action)
        viewBuffering = findViewById(R.id.exo_custom_buffering)
        viewPlay = findViewById(R.id.exo_custom_play)
        viewPause = findViewById(R.id.exo_custom_pause)

        playerView.resizeMode
        currentPlaybackView = player.playbackActionView

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.DiiaTvPlayerView,
            defStyleAttr,
            0
        ).apply {
            try {
                playerView.resizeMode = getInteger(R.styleable.DiiaTvPlayerView_playerResizeMode, 1)
            } finally {
                recycle()
            }
        }

        setOnClickListener(this)
        setupPlayerActionListeners(enable = true)
        addFlagKeepScreen(context)
    }

    fun pause() {
        clearFlagKeepScreen(context)
        player?.pause()
        updatePlaybackControlVisibility(visible = true)
    }

    private fun setupPlayerActionListeners(enable: Boolean) {
        if (enable) {
            playbackButton.setOnClickListener(this)
        } else {
            playbackButton.setOnClickListener(null)
        }
    }

    private fun updatePlaybackControlVisibility(visible: Boolean) {
        isPlaybackViewVisible = visible

        val viewVisibilityAlpha = if (visible) 1f else 0f
        updateControlPanelVisibility(viewVisibilityAlpha)

        if (visible) scheduleHideControlPanelEvent()
    }

    private fun updateControlPanelVisibility(alpha: Float) {
        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(playbackButton, "alpha", alpha),
                ObjectAnimator.ofFloat(videoDimmer, "alpha", alpha)
            )
            duration = ANIM_DURATION_CONTROL_PANEL_VISIBILITY
            start()
        }
    }

    private fun scheduleHideControlPanelEvent() {
        var job = hideControlPanelJob
        // cancel the previous job because we need to reschedule a new one with the
        //updated timing
        if (!job.isFinished) job?.cancel()

        job = lifecycleScope?.launch {
            delay(DELAY_HIDE_CONTROL_PANEL)
            if (player.allowHideControlPanel) {
                updateControlPanelVisibility(0f)
                isPlaybackViewVisible = false
            }
        }
        hideControlPanelJob = job
    }

    private fun updatePlaybackButton() {
        val newView = player.playbackActionView
        if (currentPlaybackView != newView) {
            currentPlaybackView.visibility = View.GONE
            currentPlaybackView = newView
            currentPlaybackView.visibility = View.VISIBLE
            scheduleHideControlPanelEvent()
        }
    }

    private fun executePlaybackAction() {
        when (player.playbackViewState) {
            //Starts paling content when the player is ready
            PlaybackViewState.PLAY -> {
                player?.playWhenReady = true
                addFlagKeepScreen(context)
            }
            //Pauses the video stream
            PlaybackViewState.PAUSE -> {
                player?.pause()
                clearFlagKeepScreen(context)
            }
            else -> {
            }
        }
    }


    /////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////// View listeners ////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    override fun onClick(view: View) {
        when (view) {
            //Updates the video control panel when the VideoView has been clicked
            this@NotificationVideoPlayerView -> updatePlaybackControlVisibility(!isPlaybackViewVisible)
            //Execute the playbackAction
            playbackButton -> executePlaybackAction()
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        events.onPlaybackButtonChanged(::updatePlaybackButton)
    }

    override fun onPlaybackStateChanged(playbackState: Int) {
        //shows control panel if video has been ended and control panel is not visible
        if (playbackState == Player.STATE_ENDED && !isPlaybackViewVisible) {
            updatePlaybackControlVisibility(visible = true)
        }
    }

    /////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// Util //////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////

    private val Job?.isFinished: Boolean
        get() = (this?.isCancelled ?: true) || (this?.isCompleted ?: true)

    private val Player?.isActive: Boolean
        get() = this?.isPlaying == true || this?.isLoading == true


    private val Player?.allowHideControlPanel: Boolean
        get() {
            val player = this ?: return false
            val playbackState = player.playbackState
            return isVisible && (playbackState != Player.STATE_ENDED &&
                    (player.isPlaying ||
                            playbackState == Player.STATE_BUFFERING ||
                            playbackState == Player.STATE_IDLE
                            )
                    )
        }

    private val Player?.playbackActionView: View
        get() = if (this != null) {
            when (playbackState) {
                Player.STATE_BUFFERING -> viewBuffering
                else -> if (isPlaying) viewPause else viewPlay
            }
        } else {
            viewBuffering
        }

    private val Player?.playbackViewState: PlaybackViewState
        get() = if (this != null) {
            when (playbackState) {
                Player.STATE_BUFFERING -> PlaybackViewState.BUFFERING
                else -> if (isPlaying) PlaybackViewState.PAUSE else PlaybackViewState.PLAY
            }
        } else {
            PlaybackViewState.BUFFERING
        }


    private inline fun Player.Events.onPlaybackButtonChanged(todo: () -> Unit) {
        if (containsAny(
                Player.EVENT_PLAYBACK_STATE_CHANGED,
                Player.EVENT_PLAY_WHEN_READY_CHANGED
            )
        ) {
            todo.invoke()
        }
    }
}

