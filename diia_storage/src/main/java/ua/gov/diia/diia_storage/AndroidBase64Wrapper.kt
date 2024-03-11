package ua.gov.diia.diia_storage

class AndroidBase64Wrapper: Base64Wrapper {
    override fun encode(data: ByteArray): ByteArray {
        return android.util.Base64.encode(data, android.util.Base64.NO_WRAP)
    }

    override fun decode(data: ByteArray): ByteArray {
        return android.util.Base64.decode(data, android.util.Base64.NO_WRAP)
    }
}
