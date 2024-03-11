package ua.gov.diia.core.util.extensions.lifecycle

import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*

val ViewGroup.lifecycleScope: LifecycleCoroutineScope?
    get() = findViewTreeLifecycleOwner()?.lifecycleScope

val View.lifecycleScope: LifecycleCoroutineScope?
    get() = findViewTreeLifecycleOwner()?.lifecycleScope

inline fun <reified T> MutableLiveData<T>.asLiveData(): LiveData<T> = this

fun <T> LiveData<T>.combineWith(liveData: LiveData<T>): LiveData<T> {
    val result = MediatorLiveData<T>()
    result.addSource(this) { value ->
        result.value = value
    }
    result.addSource(liveData) { value ->
        result.value = value
    }
    return result
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}