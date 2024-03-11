package ua.gov.diia.bankid.ui

import ua.gov.diia.bankid.BankIdConst.METHOD_BANK_ID
import ua.gov.diia.bankid.NavBankidDirections
import ua.gov.diia.bankid.R
import ua.gov.diia.bankid.model.BankSelectionRequest
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationRequest
import javax.inject.Inject

class VerificationMethodBankId @Inject constructor() : VerificationMethod() {

    override val name = METHOD_BANK_ID

    override val isAvailable = true

    override val iconResId = R.drawable.ic_bankid_btn

    override val titleResId = R.string.bank_id_title

    override val descriptionResId = R.string.accessibility_login_screen_bank_list_item1

    //for the BANK_ID verification we should skip the getting AUTH token step because
    //we will be able to do it only after the preferred bank will be selected and we will do
    //this operation in the BankSelectionF before showing the authentication WebView to the user
    override suspend fun getVerificationRequest(
        verificationSchema: String,
        processId: String
    ): VerificationRequest {
        val request = BankSelectionRequest(
            schema = verificationSchema,
            processId = processId,
            verificationMethodCode = name,
        )
        return VerificationRequest(
            navRequest = { currentDestinationId, resultKey ->
                NavBankidDirections.actionGlobalDestinationBankSelection(
                    resultDestination = currentDestinationId,
                    resultKey = resultKey,
                    request = request,
                )
            }
        )
    }
}