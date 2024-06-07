package ua.gov.diia.core.models.deeplink

import ua.gov.diia.core.models.acquirer.AcquirerLinkType
import ua.gov.diia.core.models.acquirer.AcquirerServiceType
import ua.gov.diia.core.models.proper_user.VerifyArgs

sealed class DeepLinkAction

interface AnyScreenDeepLinkAction

class DeepLinkActionAcquire(
    val otp: String,
    val serviceType: AcquirerServiceType,
    val linkType: AcquirerLinkType = AcquirerLinkType.dynamic
) : DeepLinkAction(), AnyScreenDeepLinkAction

class DeepLinkActionCheckDoc(
    val docName: String, val docId: String, val otp: String, val uri: String,
) : DeepLinkAction()

class DeepLinkActionViewMessage(
    val needAuth: Boolean,
    val resourceId: String,
    val notificationId: String
) : DeepLinkAction()

class DeepLinkActionStartFlow(
    val flowId: String,
    val resId: String,
    val resType: String? = null,
) : DeepLinkAction()

class DeepLinkActionOpenNotify(
    val flowType: String,
    val flowSubType: String,
    val resId: String
) : DeepLinkAction()

class DeepLinkActionProperUserVerify(
    val args: VerifyArgs
) : DeepLinkAction()

class DeepLinkActionViewDocument(
    val notificationId: String?,
    val resourceId: String?,
    val documentType: String
) : DeepLinkAction()

