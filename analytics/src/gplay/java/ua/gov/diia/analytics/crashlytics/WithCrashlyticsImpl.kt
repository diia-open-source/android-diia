package ua.gov.diia.analytics.crashlytics

import com.google.firebase.crashlytics.FirebaseCrashlytics
import ua.gov.diia.core.util.delegation.WithCrashlytics
import javax.inject.Inject

internal class WithCrashlyticsImpl @Inject constructor() : WithCrashlytics {

    private companion object {
        const val KEY_MESSAGE = "message"
    }

    override fun sendNonFatalError(e: Throwable) {
        e.message?.let { FirebaseCrashlytics.getInstance().setCustomKey(KEY_MESSAGE, it) }
        FirebaseCrashlytics.getInstance().recordException(e)
    }

    override fun sendMarkedErr(msg: String) {
        FirebaseCrashlytics.getInstance().recordException(Throwable(msg))
    }
}
