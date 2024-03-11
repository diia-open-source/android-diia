package ua.gov.diia.core.util.event


open class UiEvent {

    var hasBeenHandled = false
        protected set

    val notHandedYet
        get() = !hasBeenHandled


    fun handle() {
        hasBeenHandled = true
    }
}

open class UiDataEvent<out T>(private val content: T) : UiEvent() {

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content

    override fun toString(): String {
        return "UiDataEvent(content=$content)"
    }
}