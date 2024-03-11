package ua.gov.diia.core.util.system.service

interface SystemServiceProvider {

    fun isNfcServiceAvailable(): Boolean
}