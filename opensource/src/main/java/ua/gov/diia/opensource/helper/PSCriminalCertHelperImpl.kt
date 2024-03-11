package ua.gov.diia.opensource.helper

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.opensource.NavMainXmlDirections
import ua.gov.diia.opensource.R
import ua.gov.diia.ps_criminal_cert.helper.PSCriminalCertHelper

class PSCriminalCertHelperImpl : PSCriminalCertHelper {

    private companion object {

        const val ACTION_CERT_ORDERING_COMPLETE = "certOrdered"
    }

    override fun navigateToDamagedPropertyRecovery(
        fragment: Fragment,
        applicationId: String?
    ) {
        with(fragment){
            navigate(
                NavMainXmlDirections.actionGlobalToDamagedPropertyRecovery(
                    applicationId = applicationId
                )
            )
        }
    }

    override fun navigateToDPRecoveryHomeF(fragment: Fragment, resId: String?) {
        with(fragment){
            setNavigationResult(
                arbitraryDestination = R.id.DPRecoveryHomeF,
                key = ACTION_CERT_ORDERING_COMPLETE,
                data = resId
            )
            findNavController().popBackStack(R.id.DPRecoveryHomeF, false)
        }
    }
}