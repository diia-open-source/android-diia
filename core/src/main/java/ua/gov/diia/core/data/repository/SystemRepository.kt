package ua.gov.diia.core.data.repository

interface SystemRepository {

    suspend fun getAppVersionCode(): Int?
    suspend fun setAppVersionCode(code: Int)
}