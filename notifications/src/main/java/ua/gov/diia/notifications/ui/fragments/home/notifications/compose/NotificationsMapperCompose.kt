package ua.gov.diia.notifications.ui.fragments.home.notifications.compose

import ua.gov.diia.notifications.models.notification.pull.PullNotification
import ua.gov.diia.notifications.ui.fragments.home.notifications.compose.NotificationsActionKey.SELECT_NOTIFICATION
import ua.gov.diia.ui_base.components.molecule.message.MessageMoleculeData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import javax.inject.Inject

interface NotificationsMapperCompose {

    fun PullNotification.toComposeMessage(): MessageMoleculeData

    fun StubMessageMlcData.toComposeEmptyStateErrorMoleculeData(): StubMessageMlcData

}

class NotificationsMapperComposeImpl @Inject constructor() :
    NotificationsMapperCompose {

    override fun PullNotification.toComposeMessage(): MessageMoleculeData {
        return MessageMoleculeData(
            actionKey = SELECT_NOTIFICATION,
            title = this.pullNotificationMessage?.title,
            shortText = this.pullNotificationMessage?.shortText,
            creationDate = this.creationDate,
            isRead = this.isRead,
            notificationId = this.notificationId,
            id = this.notificationId!!,
            syncAction = this.syncAction
        )
    }

    override fun StubMessageMlcData.toComposeEmptyStateErrorMoleculeData(): StubMessageMlcData {
        return StubMessageMlcData(
            icon = this.icon,
            title = this.title
        )
    }
}