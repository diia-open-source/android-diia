package ua.gov.diia.core.util.alert

import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst

interface ClientAlertDialogsFactory {

    /**
     * For debug purposes to open verify user person at any point of app
     */
    fun userVerifySuggestion(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun nfcCardNotSupported(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun nfcResidenceCardNotSupported(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun nfcScanFailed(
        e: Exception,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
    ): TemplateDialogModel

    fun codeScanFailed(
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
    ): TemplateDialogModel

    fun nfcScanFailedV2(
        e: Exception,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
        closable: Boolean = true
    ): TemplateDialogModel

    fun alertNoInternet(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun alertVerificationFailed(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun unknownErrorAlert(
        closable: Boolean,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY,
        e: Exception
    ): TemplateDialogModel

    fun userPhotoIdTryCountReached(
        closable: Boolean,
        key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
    ): TemplateDialogModel

    fun getUnsupportedOptionDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getUnsupportedNFCDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getNoVerificationMethodsDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getResetSignaturePasswordDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun showMockGeo(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun expiredOtp(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun showAlertAfterInvalidPin(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun showAlertSignatureInvalidPin(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun showAlertAfterConfirmPin(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun showDocRemoveDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getUnsupportedGLEDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getCancelOfficialPollCreationDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getCancelledOfficialPollCreationDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun getDeletePollDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun nfcEnableDialog(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun alertNoOfflineMap(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun locationNotAvailable(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun failedToDownloadMap(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun failedToSendRating(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun failedToSendReportPoint(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun failedToSendReportShelter(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun documentUpdated(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun registerNotAvailable(docType: String?, key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel

    fun documentNotFound(key: String = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY): TemplateDialogModel
}