package ua.gov.diia.core.util.extensions.lifecycle

import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.event.UiEvent

inline fun <reified T> UiDataEvent<T>.consumeEvent(todo: (T) -> Unit){
    getContentIfNotHandled()?.let { data ->
        handle()
        todo(data)
    }
}

inline fun UiEvent.consumeEvent(todo: () -> Unit){
    if(notHandedYet){
        handle()
        todo.invoke()
    }
}