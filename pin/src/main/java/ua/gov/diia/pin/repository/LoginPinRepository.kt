package ua.gov.diia.pin.repository

interface LoginPinRepository {

    suspend fun setUserAuthorized(pin: String)

    suspend fun isPinValid(pin: String): Boolean

    suspend fun isPinPresent(): Boolean

    suspend fun getPinTryCount(): Int

    suspend fun setPinTryCount(count: Int)
}