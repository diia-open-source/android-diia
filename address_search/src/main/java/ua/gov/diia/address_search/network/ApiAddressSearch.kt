package ua.gov.diia.address_search.network

import retrofit2.http.*
import ua.gov.diia.address_search.models.AddressFieldRequest
import ua.gov.diia.address_search.models.AddressFieldResponse
import ua.gov.diia.address_search.models.AddressNationality
import ua.gov.diia.core.network.annotation.Analytics

interface ApiAddressSearch {

    @Analytics("getAddressFieldContext")
    @POST("api/v2/address/{publicService}/{addressType}")
    suspend fun getFieldContext(
        @Path("publicService") featureCode: String,
        @Path("addressType") addressTemplateCode: String,
        @Body request: AddressFieldRequest = AddressFieldRequest()
    ) : AddressFieldResponse

    @Analytics("getNationalities")
    @GET("api/v1/address/nationalities")
    suspend fun getNationalities(): AddressNationality

}