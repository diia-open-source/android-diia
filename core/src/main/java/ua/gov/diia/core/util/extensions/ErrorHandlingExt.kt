package ua.gov.diia.core.util.extensions

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException


fun Exception.noInternetException() =
    this is SocketTimeoutException || this is TimeoutException || this is UnknownHostException || this is ConnectException