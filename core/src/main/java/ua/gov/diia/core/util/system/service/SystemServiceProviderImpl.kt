package ua.gov.diia.core.util.system.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gov.diia.core.util.extensions.context.serviceNfc
import javax.inject.Inject

class SystemServiceProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SystemServiceProvider {

    override fun isNfcServiceAvailable(): Boolean = context.serviceNfc?.defaultAdapter != null
}