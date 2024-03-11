package ua.gov.diia.diia_storage

interface Base64Wrapper {

    fun encode(data: ByteArray): ByteArray

    fun decode(data: ByteArray): ByteArray
}
