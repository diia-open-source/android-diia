package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper

class PSCriminalCertHelperImpl : PSCriminalCertHelper {

    private companion object {

        const val ACTION_CERT_ORDERING_COMPLETE = "certOrdered"
    }

    override fun navigateToDamagedPropertyRecovery(
        fragment: Fragment,
        applicationId: String?
    ) {
    }

    override fun navigateToDPRecoveryHomeF(fragment: Fragment, resId: String?) {}
}