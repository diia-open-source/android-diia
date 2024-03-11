package ua.gov.diia.core.util

import ua.gov.diia.core.BuildConfig

fun throwExceptionInDebug(message: String) {
    if (BuildConfig.BUILD_TYPE == CommonConst.BUILD_TYPE_DEBUG) {
        throw Exception(message)
    }
}