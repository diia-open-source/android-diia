package ua.gov.diia.core.models.common_compose.general


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelContainerMlc
import ua.gov.diia.core.models.common_compose.atm.text.SectionTitleAtm
import ua.gov.diia.core.models.common_compose.mlc.card.ImageCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.WhiteCardMlc
import ua.gov.diia.core.models.common_compose.org.button.BtnIconRoundedGroupOrg
import ua.gov.diia.core.models.common_compose.org.carousel.ArticlePicCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.HalvedCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.SmallNotificationCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.VerticalCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.header.MediaTitleOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.core.models.notification.pull.message.ArticlePicAtm
import ua.gov.diia.core.models.notification.pull.message.ArticleVideoMlc

@JsonClass(generateAdapter = true)
data class Body(
    @Json(name = "whiteCardMlc")
    val whiteCardMlc: WhiteCardMlc?,
    @Json(name = "btnIconRoundedGroupOrg")
    val btnIconRoundedGroupOrg: BtnIconRoundedGroupOrg?,
    @Json(name = "halvedCardCarouselOrg")
    val halvedCardCarouselOrg: HalvedCardCarouselOrg?,
    @Json(name = "imageCardMlc")
    val imageCardMlc: ImageCardMlc?,
    @Json(name = "listItemGroupOrg")
    val listItemGroupOrg: ListItemGroupOrg?,
    @Json(name = "sectionTitleAtm")
    val sectionTitleAtm: SectionTitleAtm?,
    @Json(name = "smallNotificationCarouselOrg")
    val smallNotificationCarouselOrg: SmallNotificationCarouselOrg?,
    @Json(name = "verticalCardCarouselOrg")
    val verticalCardCarouselOrg: VerticalCardCarouselOrg?,
    @Json(name = "mediaTitleOrg")
    val mediaTitleOrg: MediaTitleOrg?,
    @Json(name = "articlePicCarouselOrg")
    val articlePicCarouselOrg: ArticlePicCarouselOrg?,
    @Json(name = "textLabelContainerMlc")
    val textLabelContainerMlc: TextLabelContainerMlc?,
    @Json(name = "articlePicAtm")
    val articlePicAtm: ArticlePicAtm?,
    @Json(name = "articleVideoMlc")
    val articleVideoMlc: ArticleVideoMlc?,
    )