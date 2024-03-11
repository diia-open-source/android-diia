package ua.gov.diia.core.models.common.template_dialogs

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize
import ua.gov.diia.core.util.extensions.isResourceValid

@Keep
@Parcelize
data class SystemDialogData(
    @StringRes val title: Int?,
    @StringRes val message: Int?,
    @StringRes val positiveButtonTitle: Int?,
    @StringRes val negativeButtonTitle: Int? = null,
    val cancelable: Boolean = false,
    @StringRes val rationale: Int? = null,
    @StringRes val rationaleTitle: Int? = null
) : Parcelable {

    val showMessage: Boolean
        get() = message != null

    val showTwoButtonsGroup: Boolean
        get() = negativeButtonTitle != null

    val showOneButtonGroup: Boolean
        get() = negativeButtonTitle == null

    val enableRationale: Boolean
        get() = rationale.isResourceValid() && rationaleTitle.isResourceValid()

}