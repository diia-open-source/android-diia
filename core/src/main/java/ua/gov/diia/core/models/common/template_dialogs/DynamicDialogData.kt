package ua.gov.diia.core.models.common.template_dialogs

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class DynamicDialogData(
    val title: String?,
    val message: String?,
    val positiveButtonTitle: String?,
    val negativeButtonTitle: String?,
    val cancelable: Boolean = false,
) : Parcelable {

    val showMessage: Boolean
        get() = message != null
}