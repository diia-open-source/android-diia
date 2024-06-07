package ua.gov.diia.diia_storage.store.repository.authorization

import ua.gov.diia.core.models.TokenData
import ua.gov.diia.core.models.UserType

interface AuthorizationRepository {

    suspend fun getMobileUuid(): String

    suspend fun setMobileUuid(uuid: String)

    suspend fun isUserAuthorized(): Boolean

    suspend fun logoutUser()

    suspend fun setToken(token: String)

    suspend fun getToken(): String?

    suspend fun getTokenData(): TokenData

    suspend fun setIsServiceUser(isServiceUser: Boolean)

    suspend fun setServiceUserUUID(uuid: String)

    suspend fun getServiceUserUUID(): String?

    suspend fun isServiceUser(): Boolean

    suspend fun getUserType(): UserType

}