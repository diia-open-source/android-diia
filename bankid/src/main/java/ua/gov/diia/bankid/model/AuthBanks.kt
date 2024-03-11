package ua.gov.diia.bankid.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthBanks(
    @Json(name = "banks")
    val value: List<AuthBank>?
){
    val hasBanks: Boolean
      get() = !value.isNullOrEmpty()

    val banks: List<AuthBank>
       get() = value ?: emptyList()
}