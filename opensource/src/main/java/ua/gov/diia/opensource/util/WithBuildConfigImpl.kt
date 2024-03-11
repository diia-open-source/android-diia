package ua.gov.diia.opensource.util

import android.os.Build
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.opensource.BuildConfig
import javax.inject.Inject

class WithBuildConfigImpl @Inject constructor() : WithBuildConfig {

    override fun getServerUrl(): String = BuildConfig.SERVER_URL

    override fun getBankIdHost(): String = ""

    override fun getBankIdClientId(): String = ""

    override fun getBankIdCallbackUrl(): String = BuildConfig.BANK_ID_CALLBACK_URL

    override fun getTokenLeeway(): Long = BuildConfig.TOKEN_LEEWAY

    override fun getSign(): String = ""

    override fun getDGCVerificationBaseUrl(): String = ""

    override fun getVersionCode(): Int = BuildConfig.VERSION_CODE

    override fun getVersionName(): String = BuildConfig.VERSION_NAME

    override fun getSdkVersion(): Int = Build.VERSION.SDK_INT

    override fun getBuildType(): String = BuildConfig.BUILD_TYPE

    override fun getApplicationId(): String = BuildConfig.APPLICATION_ID
}