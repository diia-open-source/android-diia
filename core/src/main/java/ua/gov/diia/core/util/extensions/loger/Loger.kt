package ua.gov.diia.core.util.extensions.loger

import android.util.Log
import ua.gov.diia.core.BuildConfig
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE

fun logD(key: String, value: String) {
    if (BuildConfig.BUILD_TYPE == BUILD_TYPE_STAGE || BuildConfig.BUILD_TYPE == BUILD_TYPE_DEBUG) {
        Log.d(key, value)
    }
}

fun logV(key: String, value: String) {
    if (BuildConfig.BUILD_TYPE == "debug") {
        Log.v(key, value)
    }
}

fun logI(key: String, value: String) {
    if (BuildConfig.BUILD_TYPE == "debug") {
        Log.i(key, value)
    }
}

fun logE(key: String, value: String) {
    if (BuildConfig.BUILD_TYPE == "debug") {
        Log.e(key, value)
    }
}

fun logW(key: String, value: String) {
    if (BuildConfig.BUILD_TYPE == "debug") {
        Log.w(key, value)
    }
}