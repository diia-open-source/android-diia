package ua.gov.diia.opensource

import com.huawei.agconnect.crash.AGConnectCrash
import ua.gov.diia.opensource.ui.activities.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VendorActivity: MainActivity() {

    override fun setUpAnalytics() {
        AGConnectCrash.getInstance().enableCrashCollection(true)
    }
}