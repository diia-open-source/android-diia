package ua.gov.diia.notifications.ui.compose.mapper.media

import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData


fun String?.toComposeArticlePic(): ArticlePicAtmData? {
    if (this == null) return null
    return ArticlePicAtmData(id = "", url = this)
}

fun String?.toComposeArticleVideo(): ArticleVideoMlcData? {
    if (this == null) return null
    return ArticleVideoMlcData(url = this)
}