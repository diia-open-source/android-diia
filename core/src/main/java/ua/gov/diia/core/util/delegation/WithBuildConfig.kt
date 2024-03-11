package ua.gov.diia.core.util.delegation

interface WithBuildConfig {
    fun getApplicationId(): String
    fun getServerUrl(): String
    fun getBankIdHost(): String
    fun getBankIdClientId(): String
    fun getBankIdCallbackUrl(): String
    fun getTokenLeeway(): Long
    fun getSign(): String
    fun getDGCVerificationBaseUrl(): String
    fun getVersionCode(): Int
    fun getVersionName(): String
    fun getSdkVersion(): Int
    fun getBuildType(): String
}