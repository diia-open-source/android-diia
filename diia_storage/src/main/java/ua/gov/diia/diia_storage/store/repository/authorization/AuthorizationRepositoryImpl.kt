package ua.gov.diia.diia_storage.store.repository.authorization

import kotlinx.coroutines.withContext
import org.json.JSONObject
import ua.gov.diia.core.models.TokenData
import ua.gov.diia.core.models.TokenData.Companion.EMPTY_TOKEN
import ua.gov.diia.core.models.UserType
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.diia_storage.Base64Wrapper
import ua.gov.diia.diia_storage.CommonPreferenceKeys
import ua.gov.diia.diia_storage.DiiaStorage
import ua.gov.diia.diia_storage.store.datasource.preferences.PreferenceDataSource
import java.util.Date
import javax.inject.Inject

class AuthorizationRepositoryImpl @Inject constructor(
    private val preferenceDataSource: PreferenceDataSource,
    private val diiaStorage: DiiaStorage,
    private val dispatcherProvider: DispatcherProvider,
    private val base64Wrapper: Base64Wrapper
) : AuthorizationRepository {

    private companion object {
        const val DEF_VALUE_STRING = "def_string"
        const val PREFERENCE_KEY_SERVICE_USER = "is_service_user"
    }

    override suspend fun getUserType(): UserType = withContext(dispatcherProvider.work) {
        if (isServiceUser()) UserType.SERVICE_USER else UserType.PRIMARY_USER
    }

    override suspend fun setIsServiceUser(isServiceUser: Boolean) =
        withContext(dispatcherProvider.work) {
            preferenceDataSource.setBoolean(PREFERENCE_KEY_SERVICE_USER, isServiceUser)
        }

    override suspend fun isServiceUser(): Boolean = withContext(dispatcherProvider.work) {
        preferenceDataSource.getBoolean(PREFERENCE_KEY_SERVICE_USER)
    }

    override suspend fun getMobileUuid(): String = withContext(dispatcherProvider.work) {
        diiaStorage.getMobileUuid()
    }

    override suspend fun setMobileUuid(uuid: String) {
        withContext(dispatcherProvider.work) {
            diiaStorage.set(CommonPreferenceKeys.UUID, uuid)
        }
    }

    override suspend fun isUserAuthorized(): Boolean = withContext(dispatcherProvider.work) {
        diiaStorage.containsKey(CommonPreferenceKeys.Token)
    }

    override suspend fun logoutUser() {
        withContext(dispatcherProvider.work) {
            diiaStorage.userLogOut()
        }
    }

    override suspend fun getTokenData(): TokenData = withContext(dispatcherProvider.work) {
        val token = diiaStorage.getString(CommonPreferenceKeys.Token, DEF_VALUE_STRING)
        if (token == DEF_VALUE_STRING) {
            TokenData(EMPTY_TOKEN, Date())
        } else {
            val tokenPayload = base64Wrapper.decode(token.split(".")[1].toByteArray())
            val tokenJson = JSONObject(String(tokenPayload))
            val expiration = Date(tokenJson.getLong(TokenData.EXP) * TokenData.SEC)
            TokenData(token, expiration)
        }
    }

    override suspend fun getToken(): String? = withContext(dispatcherProvider.work) {
        val token = diiaStorage.getString(CommonPreferenceKeys.Token, DEF_VALUE_STRING)
        if (token != DEF_VALUE_STRING) token else null
    }

    override suspend fun setToken(token: String) {
        withContext(dispatcherProvider.work) {
            diiaStorage.set(CommonPreferenceKeys.Token, token)
        }
    }


}