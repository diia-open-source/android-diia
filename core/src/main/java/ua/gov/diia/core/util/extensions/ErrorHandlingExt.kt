package ua.gov.diia.core.util.extensions

import ua.gov.diia.core.network.connectivity.NoConnectivityException

fun Exception.noInternetException() =  this is NoConnectivityException