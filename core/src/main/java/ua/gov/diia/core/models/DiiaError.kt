package ua.gov.diia.core.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class ErrorType {
    SERVER_ERROR, NETWORK_ERROR, AUTHORIZATION_ERROR, APP_OUTDATED, DEVICE_ROOTED, APP_CLONE, ALREADY_AUTHORIZED
}

@Parcelize
data class DiiaError(val urlPath: String, val exception: Exception?, val errorType: ErrorType):
    Parcelable