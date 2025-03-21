package ua.gov.diia.documents.util.datasource

import ua.gov.diia.core.util.date.CurrentDateProvider
import ua.gov.diia.core.util.extensions.date_time.getUTCDate
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.core.models.document.Expiring

/**
 * Predicate to mark item already expired
 */
interface ExpirationStrategy {
    fun isExpired(data: Any): Boolean
}

/**
 * Update if  expiration data is before current date
 * Note: you should check that data you provide implements
 * @see #ua.gov.diia.app.models.documents.Expiring
 *
 * @param currentDateProvider provides current date to compare
 */
class DateCompareExpirationStrategy(private val currentDateProvider: CurrentDateProvider) :
    ExpirationStrategy {

    private var currentDate = currentDateProvider.getDate()

    fun reset() {
        currentDate = currentDateProvider.getDate()
    }

    override fun isExpired(data: Any): Boolean {
        if (data !is Expiring) {
            throw IllegalArgumentException("Class ${data::class.java.name} must implement ${Expiring::class.java}")
        }
        if (data.getDocExpirationDate() == Preferences.DEF) {
            return true
        }
        val docExpirationDate = getUTCDate(data.getDocExpirationDate())
        return docExpirationDate?.before(currentDate) ?: true
    }
}