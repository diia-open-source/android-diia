package ua.gov.diia.opensource

import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.installations.FirebaseInstallations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ua.gov.diia.analytics.DiiaAnalytics
import ua.gov.diia.opensource.ui.activities.MainActivity
import javax.inject.Inject

@AndroidEntryPoint
class VendorActivity: MainActivity() {

    @Inject
    lateinit var analytics: DiiaAnalytics


    override fun setUpAnalytics() {
        lifecycleScope.launch(Dispatchers.IO) {
            diiaStorage.getMobileUuid().let {
                analytics.setUserId(it)
                FirebaseCrashlytics.getInstance().setUserId(it)
            }
            FirebaseInstallations.getInstance().getToken(false).addOnCompleteListener {
                try {
                    analytics.setPushToken(it.result.token)
                } catch (e: Exception) {
                    crashlytics.sendNonFatalError(e)
                }
            }
        }
    }
}