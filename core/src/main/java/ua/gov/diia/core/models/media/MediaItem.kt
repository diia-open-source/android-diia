package ua.gov.diia.core.models.media

import android.net.Uri
import androidx.annotation.ColorRes
import ua.gov.diia.core.R

data class MediaItem(
    val name: String,
    val uri: Uri,
    val type: MediaFileType,
    private var loading: Boolean = false,
    private var sizeExceeded: Boolean = false,
    private var id: String = ""
) {
    val isLoading: Boolean
        get() = loading

    val isSizeExceeded: Boolean
        get() = sizeExceeded

    fun setLoadingState(process: Boolean) {
        loading = process
    }

    fun setExceededSize(exceeded: Boolean) {
        sizeExceeded = exceeded
    }

    fun setId(newId: String) {
        id = newId
    }

    fun getId(): String {
        return id
    }

    fun getItemName(): String {
        return name
    }

    val mediaFileNameTextColor: Int
        @ColorRes
        get() = if (!sizeExceeded && !loading) {
            R.color.black
        } else if (!sizeExceeded) {
            R.color.black_alpha_30
        } else {
            R.color.black_alpha_30
        }
}