package ua.gov.diia.core.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConsumableString(
    val item: String?,
    var isConsumed: Boolean = false
) : Parcelable {

    fun isNotConsumed() = !isConsumed

    fun consumeEvent(action: (String) -> Unit) {
        if (!isConsumed) {
            isConsumed = true
            action.invoke(item ?: "")
        }
    }
}