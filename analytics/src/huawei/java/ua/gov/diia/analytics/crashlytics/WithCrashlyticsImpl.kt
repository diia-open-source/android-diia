package ua.gov.diia.analytics.crashlytics

import com.huawei.agconnect.crash.AGConnectCrash
import ua.gov.diia.core.util.delegation.WithCrashlytics

internal class WithCrashlyticsImpl : WithCrashlytics {

    override fun sendNonFatalError(e: Throwable) {
        runCatching {
            AGConnectCrash.getInstance().recordException(e)
        }
    }

    override fun sendMarkedErr(msg: String) {
        AGConnectCrash.getInstance().log(msg)
    }
}