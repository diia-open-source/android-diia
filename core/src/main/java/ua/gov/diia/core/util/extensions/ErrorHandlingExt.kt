package ua.gov.diia.core.util.extensions

import java.net.ConnectException

fun Exception.noInternetException() = this is ConnectException