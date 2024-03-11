package ua.gov.diia.bankid.network

import retrofit2.http.GET
import ua.gov.diia.bankid.model.AuthBanks
import ua.gov.diia.core.network.annotation.Analytics

interface ApiBankId {

    @Analytics("getBanksList")
    @GET("api/v1/auth/banks")
    suspend fun getBanksList(): AuthBanks

}