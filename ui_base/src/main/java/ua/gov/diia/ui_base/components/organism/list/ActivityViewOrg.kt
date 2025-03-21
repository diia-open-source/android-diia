package ua.gov.diia.ui_base.components.organism.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.BtnAlertAdditionalAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtm
import ua.gov.diia.ui_base.components.atom.button.BtnWhiteLargeAtmData
import ua.gov.diia.ui_base.components.atom.button.ButtonIconCircledLargeAtmData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlc
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.CardHorizontalScrollData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.organism.carousel.CardHorizontalScrollOrg
import ua.gov.diia.ui_base.components.organism.carousel.CardHorizontalScrollOrgData
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons

@Composable
fun ActivityViewOrg(
    modifier: Modifier = Modifier,
    data: ActivityViewOrgData,
    onUIAction: (UIAction) -> Unit
) {
    Column(
        modifier = modifier
            .conditional(data.alertCardMlcData == null && data.cardHorizontalScrollOrgData == null) {
                padding(horizontal = if (data.existForDocs) 16.dp else 24.dp)
            }
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        data.contextMenuOrg?.let {
            ContextIconMenuOrg(
                modifier = Modifier.conditional(data.alertCardMlcData != null) {
                    padding(horizontal = 24.dp)
                },
                data = it, onUIAction = onUIAction
            )
        }
        data.alertCardMlcData?.let {
            AlertCardMlc(
                data = it,
                onUIAction = onUIAction
            )
        }
        data.cardHorizontalScrollOrgData?.let {
            CardHorizontalScrollOrg(
                data = it,
                onUIAction = onUIAction
            )
        }
        BtnWhiteLargeAtm(
            modifier = Modifier
                .padding(top = 8.dp)
                .conditional(data.alertCardMlcData != null || data.cardHorizontalScrollOrgData != null) {
                    padding(horizontal = 24.dp)
                }
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth()
                .wrapContentHeight(),
            data = data.button,
            onUIAction = onUIAction
        )
    }
}

data class ActivityViewOrgData(
    val contextMenuOrg: ContextIconMenuOrgData?,
    val alertCardMlcData: AlertCardMlcData? = null,
    val cardHorizontalScrollOrgData: CardHorizontalScrollOrgData? = null,
    val button: BtnWhiteLargeAtmData,
    val existForDocs: Boolean = true
) : UIElementData

@Preview
@Composable
fun ActivityViewOrgPreview() {
    val itemsList = SnapshotStateList<ListItemMlcData>().apply {
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code)
            )
        )
        add(
            ListItemMlcData(
                label = UiText.DynamicString("Label"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code)
            )
        )
    }
    val qr = ButtonIconCircledLargeAtmData(
        id = "qr",
        label = "Label",
        icon = UiText.StringResource(R.drawable.ic_doc_qr_selected)
    )
    val ean13 = ButtonIconCircledLargeAtmData(
        id = "ean",
        label = "Label",
        icon = UiText.StringResource(R.drawable.ic_doc_ean13_selected),
    )
    val contextIconMenuOrgData = ContextIconMenuOrgData(null,itemsList, itemsList, null, qr, ean13, true)
    val button = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    val data = ActivityViewOrgData(contextMenuOrg = contextIconMenuOrgData, button = button)
    val state = remember {
        mutableStateOf(data)
    }
    ActivityViewOrg(data = state.value) {

    }
}

@Preview
@Composable
fun ActivityViewOrgPreview_WithAlert() {
    val alertCard = AlertCardMlcData(
        iconText = UiText.DynamicString("⚠️"),
        label = UiText.DynamicString("Товар не бере участь у програмі Зроблено в Україні"),
        text = UiText.DynamicString("Номер штрихкоду: 4750042804321."),
        alertBtn = BtnAlertAdditionalAtmData(title = UiText.DynamicString("Сповістити"))
    )
    val button = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    val data =
        ActivityViewOrgData(alertCardMlcData = alertCard, button = button, contextMenuOrg = null)


    val state = remember {
        mutableStateOf(data)
    }
    ActivityViewOrg(data = state.value) {

    }

}

@Preview
@Composable
fun ActivityViewOrgPreview_CardHorizontalScroll() {
    val cardHorizontalScrollOrgData = CardHorizontalScrollOrgData(
        items = MutableList(3) {
            CardHorizontalScrollData(
                itemsList = mutableStateListOf(
                    ListItemMlcData(
                        label = UiText.DynamicString("label label label label label label label label label label label label label label label label label label label label"),
                        description = UiText.DynamicString("label label label label label label label label label label label label label label label")
                    ),
                    ListItemMlcData(
                        label = UiText.DynamicString("Label"),
                        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple)
                    )
                )
            )
        }
    )
    val button = BtnWhiteLargeAtmData(
        title = "Label".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    val data = ActivityViewOrgData(alertCardMlcData = null, button = button, contextMenuOrg = null, cardHorizontalScrollOrgData = cardHorizontalScrollOrgData)


    val state = remember {
        mutableStateOf(data)
    }
    ActivityViewOrg(data = state.value) {

    }

}


@Preview
@Composable
fun ActivityViewOrgPreview_WithContextMenu() {
    val contextMenuOrgData = ContextIconMenuOrgData(
        displayItems = listOf(
            ListItemMlcData(
                label = UiText.DynamicString("Зробити фото"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.CAMERA.code),
            ),
            ListItemMlcData(
                label = UiText.DynamicString("Обрати з галереї"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.GALLERY.code),
            ),
            ListItemMlcData(
                label = UiText.DynamicString("Додати фото з документа"),
                iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.DOC_INFO.code),
            )
        ),
        qr = null,
        showButtons = false,
        ean13 = null
    )

    val button = BtnWhiteLargeAtmData(
        title = "Закрити".toDynamicString(),
        id = "",
        interactionState = UIState.Interaction.Enabled
    )
    val data = ActivityViewOrgData(
        contextMenuOrg = contextMenuOrgData,
        button = button
    )

    val state = remember {
        mutableStateOf(data)
    }
    ActivityViewOrg(data = state.value) {

    }

}