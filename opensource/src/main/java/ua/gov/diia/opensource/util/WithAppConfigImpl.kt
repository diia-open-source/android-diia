package ua.gov.diia.opensource.util

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import ua.gov.diia.core.util.delegation.WithAppConfig
import ua.gov.diia.opensource.R
import javax.inject.Inject

class WithAppConfigImpl @Inject constructor(
    @ApplicationContext private val context: Context,
): WithAppConfig {

    override fun getAppPolicyUrl(): String = context.getString(R.string.url_app_policy)

    override fun getAboutUrl(): String  = context.getString(R.string.url_about_diia)
}