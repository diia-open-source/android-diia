package ua.gov.diia.ui_base.components.atom.text.textwithparameter

import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText


data class TextParameter(
    val data: Data?,
    val type: String?
) {
    data class Data(
        val alt: UiText?,
        val name: UiText?,
        val resource: UiText?
    )
}