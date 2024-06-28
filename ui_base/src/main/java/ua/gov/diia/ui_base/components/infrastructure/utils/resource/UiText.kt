package ua.gov.diia.ui_base.components.infrastructure.utils.resource

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    class StringResource(val resId: Int, vararg val args: Any) : UiText()


    @Composable
    fun asString(): String {
        return this.let {
            when (it) {
                is DynamicString -> it.value
                is StringResource -> stringResource(id = it.resId, formatArgs = it.args)
            }
        }
    }

    fun hasValue(): Boolean {
        return when(this){
            is DynamicString -> this.value.isEmpty()
            is StringResource -> true
        }
    }
}

fun String?.toDynamicStringOrNull(): UiText.DynamicString? {
    if (this == null) return null
    return UiText.DynamicString(this)
}

fun String?.toDynamicString(): UiText.DynamicString {
    return UiText.DynamicString(this ?: "")
}
