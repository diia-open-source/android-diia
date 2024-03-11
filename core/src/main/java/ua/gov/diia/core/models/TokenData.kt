package ua.gov.diia.core.models

import java.util.*

data class TokenData(val token: String, val tokenExp: Date) {

    val isEmptyToken: Boolean
        get() = token == EMPTY_TOKEN

    fun isExpired(tokenLeeway: Long): Boolean{
        val now = Date()
        val leewayDate = Date(tokenExp.time - tokenLeeway * SEC)
        val expValid = now.before(leewayDate)
        return !expValid
    }

    companion object {
        const val EMPTY_TOKEN = "empty_token"
        const val SEC = 1_000
        const val EXP = "exp"
    }
}