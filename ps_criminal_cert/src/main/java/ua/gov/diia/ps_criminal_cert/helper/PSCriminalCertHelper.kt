package ua.gov.diia.ps_criminal_cert.helper

import androidx.fragment.app.Fragment

interface PSCriminalCertHelper {

   /**
    * @return navigation action to Damaged Property Recovery public service
    * */
   fun navigateToDamagedPropertyRecovery(fragment: Fragment, applicationId: String?)

   /**
    * @return navigation action to go back to the Damaged Property Recovery public service with result
    * */
   fun navigateToDPRecoveryHomeF(fragment: Fragment, resId: String?)
}