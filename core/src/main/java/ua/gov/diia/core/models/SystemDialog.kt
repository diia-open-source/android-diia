package ua.gov.diia.core.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SystemDialog(
    val title: String?,
    val message: String?,
    val positiveButtonTitle: String?,
    val negativeButtonTitle: String? = null,
    val cancelable: Boolean = false
) : Parcelable