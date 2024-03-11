package ua.gov.diia.core.util.date

import ua.gov.diia.core.util.extensions.date_time.getCurrentDateUtc
import java.util.Date


interface CurrentDateProvider {

    fun getDate(): Date
}

internal class CurrentDateProviderImpl : CurrentDateProvider {

    override fun getDate(): Date {
        return getCurrentDateUtc()
    }
}
