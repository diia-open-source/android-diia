package ua.gov.diia.core.models.share

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShareByteArr(
    @Json(name = "fileName")
    val fileName: String,
    @Json(name = "byteArray")
    val byteArray: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ShareByteArr

        if (fileName != other.fileName) return false
        if (!byteArray.contentEquals(other.byteArray)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = fileName.hashCode()
        result = 31 * result + byteArray.contentHashCode()
        return result
    }
}
