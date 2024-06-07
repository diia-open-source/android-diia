package ua.gov.diia.core.models.common_compose.general

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import ua.gov.diia.core.models.common.message.AttentionMessageMlc
import ua.gov.diia.core.models.common.message.StatusMessage
import ua.gov.diia.core.models.common_compose.atm.SpacerAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnPrimaryWideAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeDefaultAtm
import ua.gov.diia.core.models.common_compose.atm.button.BtnStrokeWhiteAtm
import ua.gov.diia.core.models.common_compose.atm.text.SectionTitleAtm
import ua.gov.diia.core.models.common_compose.mlc.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.card.CardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.ImageCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.WhiteCardMlc
import ua.gov.diia.core.models.common_compose.mlc.divider.DividerLineMlc
import ua.gov.diia.core.models.common_compose.mlc.text.StubMessageMlc
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelContainerMlc
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelMlc
import ua.gov.diia.core.models.common_compose.mlc.text.TitleLabelMlc
import ua.gov.diia.core.models.common_compose.org.button.BtnIconRoundedGroupOrg
import ua.gov.diia.core.models.common_compose.org.card.ServiceCardTileOrg
import ua.gov.diia.core.models.common_compose.org.carousel.ArticlePicCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.HalvedCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.SmallNotificationCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.VerticalCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxBtnOrg
import ua.gov.diia.core.models.common_compose.org.checkbox.CheckboxRoundGroupOrg
import ua.gov.diia.core.models.common_compose.org.header.MediaTitleOrg
import ua.gov.diia.core.models.common_compose.org.input.question_form.QuestionFormsOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.core.models.common_compose.org.media.FileUploadGroupOrg
import ua.gov.diia.core.models.common_compose.org.media.FullScreenVideoOrg
import ua.gov.diia.core.models.common_compose.org.media.GroupFilesAddOrg
import ua.gov.diia.core.models.common_compose.org.media.MediaUploadGroupOrg
import ua.gov.diia.core.models.common_compose.org.radioBtn.RadioBtnGroupOrg
import ua.gov.diia.core.models.common_compose.org.sharing.SharingCodesOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockAccordionOrg.TableBlockAccordionOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockOrg.TableBlockOrg
import ua.gov.diia.core.models.common_compose.table.tableBlockPlaneOrg.TableBlockPlaneOrg
import ua.gov.diia.core.models.notification.pull.message.ArticlePicAtm
import ua.gov.diia.core.models.notification.pull.message.ArticleVideoMlc

@JsonClass(generateAdapter = true)
data class Body(
    @Json(name = "whiteCardMlc")
    val whiteCardMlc: WhiteCardMlc? = null,
    @Json(name = "btnIconRoundedGroupOrg")
    val btnIconRoundedGroupOrg: BtnIconRoundedGroupOrg? = null,
    @Json(name = "halvedCardCarouselOrg")
    val halvedCardCarouselOrg: HalvedCardCarouselOrg? = null,
    @Json(name = "imageCardMlc")
    val imageCardMlc: ImageCardMlc? = null,
    @Json(name = "listItemGroupOrg")
    val listItemGroupOrg: ListItemGroupOrg? = null,
    @Json(name = "sectionTitleAtm")
    val sectionTitleAtm: SectionTitleAtm? = null,
    @Json(name = "smallNotificationCarouselOrg")
    val smallNotificationCarouselOrg: SmallNotificationCarouselOrg? = null,
    @Json(name = "verticalCardCarouselOrg")
    val verticalCardCarouselOrg: VerticalCardCarouselOrg? = null,
    @Json(name = "mediaTitleOrg")
    val mediaTitleOrg: MediaTitleOrg? = null,
    @Json(name = "articlePicCarouselOrg")
    val articlePicCarouselOrg: ArticlePicCarouselOrg? = null,
    @Json(name = "textLabelContainerMlc")
    val textLabelContainerMlc: TextLabelContainerMlc? = null,
    @Json(name = "articlePicAtm")
    val articlePicAtm: ArticlePicAtm? = null,
    @Json(name = "articleVideoMlc")
    val articleVideoMlc: ArticleVideoMlc? = null,
    //NEW
    @Json(name = "titleLabelMlc")
    val titleLabelMlc: TitleLabelMlc? = null,
    @Json(name = "textLabelMlc")
    val textLabelMlc: TextLabelMlc? = null,
    @Json(name = "attentionMessageMlc")
    val attentionMessageMlc: AttentionMessageMlc? = null,
    @Json(name = "stubMessageMlc")
    val stubMessageMlc: StubMessageMlc? = null, //todo Has two Stub
    @Json(name = "statusMessageMlc")
    val statusMessageMlc: StatusMessage? = null,
    @Json(name = "tableBlockOrg")
    val tableBlockOrg: TableBlockOrg? = null,
    @Json(name = "tableBlockPlaneOrg")
    val tableBlockPlaneOrg: TableBlockPlaneOrg? = null,
    @Json(name = "tableBlockAccordionOrg")
    val tableBlockAccordionOrg: TableBlockAccordionOrg? = null,
    @Json(name = "fullScreenVideoOrg")
    val fullScreenVideoOrg: FullScreenVideoOrg? = null,
    @Json(name = "cardMlc")
    val cardMlc: CardMlc? = null,
    @Json(name = "questionFormsOrg")
    val questionFormsOrg: QuestionFormsOrg? = null,
    @Json(name = "checkboxRoundGroupOrg")
    val checkboxRoundGroupOrg: CheckboxRoundGroupOrg? = null,
    @Json(name = "radioBtnGroupOrg")
    val radioBtnGroupOrg: RadioBtnGroupOrg? = null,
    @Json(name = "btnPrimaryDefaultAtm")
    val btnPrimaryDefaultAtm: BtnPrimaryDefaultAtm? = null,
    @Json(name = "btnStrokeDefaultAtm")
    val btnStrokeDefaultAtm: BtnStrokeDefaultAtm? = null,
    @Json(name = "btnStrokeWhiteAtm")
    val btnStrokeWhiteAtm: BtnStrokeWhiteAtm? = null,
    @Json(name = "btnPrimaryWideAtm")
    val btnPrimaryWideAtm: BtnPrimaryWideAtm? = null,
    @Json(name = "btnPlainAtm")
    val btnPlainAtm: BtnPlainAtm? = null,
    @Json(name = "checkboxBtnOrg")
    val checkboxBtnOrg: CheckboxBtnOrg? = null,
    @Json(name = "spacerAtm")
    val spacerAtm: SpacerAtm? = null,
    @Json(name = "serviceCardTileOrg")
    val serviceCardTileOrg: ServiceCardTileOrg? = null,
    @Json(name = "mediaUploadGroupOrg")
    val mediaUploadGroupOrg: MediaUploadGroupOrg? = null,
    @Json(name = "sharingCodesOrg")
    val sharingCodesOrg: SharingCodesOrg? = null,
    @Json(name = "fileUploadGroupOrg")
    val fileUploadGroupOrg: FileUploadGroupOrg? = null,
    @Json(name = "groupFilesAddOrg")
    val groupFilesAddOrg: GroupFilesAddOrg? = null,
    @Json(name = "btnLoadIconPlainGroupMlc")
    val btnLoadIconPlainGroupMlc: BtnLoadIconPlainGroupMlc? = null,
    @Json(name = "dividerLineMlc")
    val dividerLineMlc: DividerLineMlc? = null,
    )