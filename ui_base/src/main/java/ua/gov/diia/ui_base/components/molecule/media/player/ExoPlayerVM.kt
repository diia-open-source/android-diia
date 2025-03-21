package ua.gov.diia.ui_base.components.molecule.media.player

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExoPlayerVM @Inject constructor(
    val player: ExoPlayer
) : ViewModel() {

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress.asStateFlow()

    init {
        player.prepare()
        viewModelScope.launch {
            while (isActive) {
                if (player.isPlaying) {
                    _progress.value = player.currentPosition
                }
                delay(50)
            }
        }
    }

    fun addVideoUri(url: String) {
        player.setMediaItem(
            MediaItem.Builder()
                .setUri(url)
                .setMimeType(MimeTypes.VIDEO_MP4)
                .build()
        )
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}