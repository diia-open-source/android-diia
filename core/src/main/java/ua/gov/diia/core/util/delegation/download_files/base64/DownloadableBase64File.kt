package ua.gov.diia.core.util.delegation.download_files.base64

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DownloadableBase64File(
    @Json(name = "file")
    val file: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "mimeType")
    val mimeType: String
)