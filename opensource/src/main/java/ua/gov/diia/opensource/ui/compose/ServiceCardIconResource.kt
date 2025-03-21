package ua.gov.diia.opensource.ui.compose

import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon

enum class ServiceCardIconResource(
    val code: String,
    val drawableResourceId: Int,
    val contentDescriptionResourceId: Int
) {
    PS_CERTIFICATES(
        "certificates",
        R.drawable.ic_ps_certificates,
        R.string.ps_icon_description_certificates
    );


    companion object {

        fun getResourceId(code: String): Int {
            return entries
                .firstOrNull { code == it.code }?.drawableResourceId
                ?: DiiaResourceIcon.DEFAULT.drawableResourceId
        }

        fun getContentDescription(code: String): Int {
            return entries
                .firstOrNull { code == it.code }?.contentDescriptionResourceId
                ?: DiiaResourceIcon.DEFAULT.contentDescriptionResourceId
        }
    }
}