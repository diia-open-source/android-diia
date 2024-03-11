package ua.gov.diia.core.models

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ConsumableItem(
    val item: Parcelable,
    var isConsumed: Boolean = false
) : Parcelable {

    fun isNotConsumed() = !isConsumed

    inline fun <reified T : Parcelable> consumeEvent(action: (T) -> Unit) {
        if (!isConsumed && item is T) {
            isConsumed = true
            action.invoke(item)
        }
    }

}

@Parcelize
class ConsumableEvent(var isConsumed: Boolean = false) : Parcelable {

    fun consumeEvent(action: () -> Unit) {
        if (!isConsumed) {
            isConsumed = true
            action.invoke()
        }
    }
}