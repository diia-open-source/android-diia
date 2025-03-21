package ua.gov.diia.core.network.connectivity

import java.io.IOException

class NoConnectivityException : IOException() {

    override val message: String
        get() = "No Internet Connection"

}