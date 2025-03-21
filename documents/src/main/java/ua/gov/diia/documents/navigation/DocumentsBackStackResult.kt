package ua.gov.diia.documents.navigation

import ua.gov.diia.core.models.ConsumableEvent
import ua.gov.diia.core.models.ConsumableItem
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.common.BackStackEvent

data class VerificationCodeBackStackResult(val data: ConsumableItem) : BackStackEvent

data class RemoveDocumentBackStackResult(val data: ConsumableItem) : BackStackEvent

data class RateDocumentBackStackResult(val data: ConsumableItem) : BackStackEvent

data class QRCodeBackStackResult(val data: ConsumableItem) : BackStackEvent

data class Earn13CodeBackStackResult(val data: ConsumableItem) : BackStackEvent

data class DownloadCerfPdfBackStackResult(val data: ConsumableItem) : BackStackEvent

data class DownloadPdfBackStackResult(val data: ConsumableItem) : BackStackEvent

data class RemoveDocBackStackResult(val data: ConsumableItem) : BackStackEvent

data class ShowRemoveDocBackStackResult(val data: ConsumableItem) : BackStackEvent

data class ConfirmRemoveDocBackStackResult(val data: ConsumableItem) : BackStackEvent

data class StartFlowIfDocExistBackStackResult(val data: ConsumableItem) : BackStackEvent

data class ConfirmDocShareBackStackResult(val data: ConsumableString) : BackStackEvent

data class AddDocBackStackResult(val data: ConsumableEvent) : BackStackEvent

data class ShareDocBackStackResult(val data: ConsumableItem) : BackStackEvent

data class UpdateDocBackStackResult(val data: ConsumableItem) : BackStackEvent

data class DocUserActionResult(val data: ConsumableItem) : BackStackEvent