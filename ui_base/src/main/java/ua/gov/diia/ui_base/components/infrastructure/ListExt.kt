package ua.gov.diia.ui_base.components.infrastructure

import androidx.compose.runtime.snapshots.SnapshotStateList

fun <T> SnapshotStateList<T>.addIfNotNull(element: T?) : SnapshotStateList<T> where T : UIElementData {
    if (element != null) {
        this.add(element)
    }
    return this
}

fun <T> SnapshotStateList<T>.addAllIfNotNull(vararg elements: T?) : SnapshotStateList<T> where T : UIElementData {
    elements.forEach {
        it?.let {
            this.add(it)
        }
    }
    return this
}

fun <T> SnapshotStateList<T>.addAllFromListIfNotNull(list: List<T>) : SnapshotStateList<T> where T : UIElementData {
    list.forEach {
        it?.let {
            this.add(it)
        }
    }
    return this
}

inline fun <reified T> SnapshotStateList<UIElementData>.findAndChangeFirstByInstance(action: (T) -> UIElementData) {
    val index = this.indexOfFirst { it is T }

    if (index == -1) {
        return
    } else {
        this[index] = action.invoke(this[index] as T)
    }
}