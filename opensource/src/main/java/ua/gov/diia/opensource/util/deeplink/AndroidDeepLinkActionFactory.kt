package ua.gov.diia.opensource.util.deeplink

import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.deeplink.DeepLinkActionOpenNotify
import ua.gov.diia.core.models.deeplink.DeepLinkActionStartFlow
import ua.gov.diia.core.models.deeplink.DeepLinkActionViewDocument
import ua.gov.diia.core.models.deeplink.DeepLinkActionViewMessage
import ua.gov.diia.core.models.notification.push.PushNotification
import ua.gov.diia.core.util.deeplink.DeepLinkActionFactory
import ua.gov.diia.diia_storage.store.Preferences
import ua.gov.diia.doc_driver_license.models.DocName.DRIVER_LICENSE
import javax.inject.Inject

class AndroidDeepLinkActionFactory @Inject constructor() :
    DeepLinkActionFactory {

    override fun buildDeepLinkAction(path: String): DeepLinkAction {
        if (path.startsWith(DEEP_LINK_DOCUMENT_CHECK_PREFIX)) {
            val checkDocParams = path.split(SPLIT)
            if (checkDocParams.size > 1) {
                val docType: String? = mapCorrectDocNameType(checkDocParams[2])
                if (docType != null) {
                    return DeepLinkActionViewDocument(
                        documentType = docType,
                        resourceId = checkDocParams.getOrLogEvent(3, path),
                        notificationId = checkDocParams.getOrLogEvent(4, path),
                    )
                }
            }
        }

        if (path.startsWith(DEEP_LINK_MESSAGE)) {
            val checkDocParams = path.split(SPLIT)
            if (checkDocParams.size > 4) {
                return DeepLinkActionViewMessage(
                    needAuth = checkDocParams[2].toBoolean(),
                    notificationId = checkDocParams[3],
                    resourceId = checkDocParams[4],
                )
            }
        }

        if (path.startsWith(DEEP_LINK_OPEN_PULL_NOTIFICATION)) {
            val params = path.split(SPLIT)
            if (params.size > 2) {
                val flowType = params[SECOND_DEEP_LINK_COLUMN]

                val flowSubType = if (params.size > 3) {
                    params[THIRD_DEEP_LINK_COLUMN]
                } else {
                    ""
                }

                val resId = if (params.size > 4) {
                    params[FOURTH_DEEP_LINK_COLUMN]
                } else {
                    Preferences.DEF
                }
                return DeepLinkActionOpenNotify(
                    flowType = flowType,
                    flowSubType = flowSubType,
                    resId = resId
                )
            }
        }

        val params = path.split(SPLIT)
        var flowId = DEEP_LINK_HOME
        var resId = Preferences.DEF
        if (params.size > 1) {
            flowId = params[FIRST_DEEP_LINK_COLUMN]
            resId = if (params.size > 3) {
                params[THIRD_DEEP_LINK_COLUMN]
            } else if (params.size > 2) {
                params[SECOND_DEEP_LINK_COLUMN]
            } else Preferences.DEF
        }
        return DeepLinkActionStartFlow(
            flowId,
            resId
        )
    }

    private fun mapCorrectDocNameType(name: String) = when (name) {
        "driverLicense" -> DRIVER_LICENSE
        else -> null
    }

    override fun buildPathFromPushNotification(pushNotification: PushNotification): String {
        return when (pushNotification.action.type) {
            else -> "$DIIA_HTTPS$DEEP_LINK_OPEN_PULL_NOTIFICATION/${pushNotification.action.type}/${pushNotification.action.subtype ?: ""}/${pushNotification.action.resourceId ?: ""}"
        }
    }

    private fun List<String>.getOrLogEvent(index: Int, rootPath: String): String {
        val result = this.getOrNull(index)
        return result ?: ""
    }

    companion object {
        private const val DEEP_LINK_HOME = "/home/"
        private const val DEEP_LINK_DOCUMENT_CHECK_PREFIX = "/documents/"

        private const val DEEP_LINK_MESSAGE = "/message/"
        private const val DEEP_LINK_OPEN_PULL_NOTIFICATION = "/open-pull-notification"

        private const val SPLIT = "/"
        private const val FIRST_DEEP_LINK_COLUMN = 1
        private const val SECOND_DEEP_LINK_COLUMN = 2
        private const val THIRD_DEEP_LINK_COLUMN = 3
        private const val FOURTH_DEEP_LINK_COLUMN = 4

        private const val DIIA_HTTPS = "https://diia.app"
    }
}