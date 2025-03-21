package ua.gov.diia.opensource.action

import androidx.navigation.NavDirections
import ua.gov.diia.core.models.notification.pull.PullNotificationItemSelection
import ua.gov.diia.core.push.BasePushNotificationAction
import ua.gov.diia.opensource.NavMainDirections

class CriminalCertNotificationAction : BasePushNotificationAction("criminalRecordCertificate") {

    override fun getNavigationDirection(item: PullNotificationItemSelection): NavDirections =
        NavMainDirections.actionHomeFToCriminalCert(certId = item.resourceId)
}