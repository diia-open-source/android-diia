package ua.gov.diia.core.util.delegation

interface WithCrashlytics {
    fun sendNonFatalError(e: Throwable)

    fun sendMarkedErr(msg: String)
}