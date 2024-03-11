package ua.gov.diia.ui_base.fragments.errordialog

data class RequestTryCountTracker(var tryCount: Int = 0) {
    fun increment() {
        tryCount++
    }

    fun reset() {
        tryCount = 0
    }
}