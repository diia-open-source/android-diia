package ua.gov.diia.diia_storage

interface PinStore {

    suspend fun savePin(pin: String)

    suspend fun isPinValid(pinInput: String): Boolean

    fun pinPresent(): Boolean

    fun clear()

}
