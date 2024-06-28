package ua.gov.diia.diia_storage

interface ServiceUserUidStore {

    suspend fun saveServiceUserUUID(uuid: String)

    suspend fun getServiceUserUUID(): String?

    fun clear()

}