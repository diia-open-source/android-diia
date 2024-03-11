package ua.gov.diia.documents.ui.gallery

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import ua.gov.diia.documents.ui.DocsConst.DOCUMENT_TYPE_ALL

@Keep
@Parcelize
data class DocFSettings(
    val gradientBackgroundEnabled: Boolean = false,
    val documentType: String = DOCUMENT_TYPE_ALL,
) : Parcelable {

    companion object {

        fun default(): DocFSettings {
            return DocFSettings(true, DOCUMENT_TYPE_ALL)
        }

        val default = DocFSettings(true)

    }
}