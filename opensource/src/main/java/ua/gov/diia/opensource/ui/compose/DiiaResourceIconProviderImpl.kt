package ua.gov.diia.opensource.ui.compose

import ua.gov.diia.opensource.R
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider

class DiiaResourceIconProviderImpl : DiiaResourceIconProvider {

    private val commonIcons = CommonDiiaResourceIcon.diiaResourceIconList()
    private val publicServiceIcons = listOf(
        DiiaResourceIcon(
            "donation",
            R.drawable.ic_ps_military_donation,
            R.string.ps_icon_description_donation
        ),

        DiiaResourceIcon(
            "certificates",
            R.drawable.ic_ps_certificates,
            R.string.ps_icon_description_certificates
        ),
    )


    override fun getResourceId(code: String): Int {
        return (commonIcons + publicServiceIcons).firstOrNull { code == it.code }?.drawableResourceId
            ?: CommonDiiaResourceIcon.DEFAULT.drawableResourceId
    }

    override fun getContentDescription(code: String): Int {
        return (commonIcons + publicServiceIcons).firstOrNull { code == it.code }?.contentDescriptionResourceId
            ?: CommonDiiaResourceIcon.DEFAULT.contentDescriptionResourceId
    }
}