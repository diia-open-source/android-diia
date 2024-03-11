package ua.gov.diia.biometric.store

interface BiometricRepository {

    suspend fun enableBiometricAuth(enable: Boolean)

    suspend fun isBiometricAuthEnabled(): Boolean

}