package ua.gov.diia.opensource.data.repository.settings


interface AppSettingsRepository {

    suspend fun enableBiometricAuth(enable: Boolean)

    suspend fun isBiometricAuthEnabled(): Boolean

    suspend fun getLastDocumentUpdate(): String?

    suspend fun getLastActiveDate(): String?
}