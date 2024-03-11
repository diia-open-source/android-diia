package ua.gov.diia.biometric

interface Biometric {

    fun isBiometricAuthAvailable(): Boolean

    fun hasFingerprint(): Boolean

    fun hasFace(): Boolean

    fun getCurrentBiometricTypeInUse(): Int

    fun getPromptDescriptionString(): Int

    companion object {

        const val NONE_IN_USE = -1
        const val FINGERPRINT_IN_USE = 0
        const val FACE_IN_USE = 1
    }
}