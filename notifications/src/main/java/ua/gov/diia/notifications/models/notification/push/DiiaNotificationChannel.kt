package ua.gov.diia.notifications.models.notification.push

import androidx.annotation.StringRes
import ua.gov.diia.notifications.R

enum class DiiaNotificationChannel(val id: String, @StringRes val label: Int) {
    ACQUIRER("acquirer", R.string.notification_channel_acquirer),
    MESSAGE("messages", R.string.notification_channel_messages),
    PENALTIES("penalties", R.string.notification_channel_penalties),
    DEBTS("debts", R.string.notification_channel_debts),
    DEFAULT("diia", R.string.notification_channel_diia_default)
}