package ua.gov.diia.ui_base.components.organism.document

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.button.ButtonStrokeAdditionalAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerAtm
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.TickerType
import ua.gov.diia.ui_base.components.atom.text.TickerUsage
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlc
import ua.gov.diia.ui_base.components.molecule.doc.DocCoverMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.TableItemVerticalMlcData
import ua.gov.diia.ui_base.components.molecule.list.table.items.tableblock.toTableMainHeadingMlcData
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlcData
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlc
import ua.gov.diia.ui_base.components.molecule.tile.SmallEmojiPanelMlcData
import ua.gov.diia.ui_base.components.organism.pager.CardFocus
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Images
import ua.gov.diia.ui_base.components.theme.White
import ua.gov.diia.ui_base.components.theme.WhiteAlpha25
import ua.gov.diia.ui_base.components.theme.WhiteAlpha40
import ua.gov.diia.ui_base.components.theme.WhiteAlpha50

@Composable
fun DocPhotoOrg(
    modifier: Modifier,
    data: DocPhotoOrgData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    cardFocus: CardFocus = CardFocus.UNDEFINED,
    onUIAction: (UIAction) -> Unit
) {
    val tickerDisplayed = remember { mutableStateOf(cardFocus != CardFocus.OUT_OF_FOCUS) }

    LaunchedEffect(key1 = cardFocus) {
        tickerDisplayed.value = cardFocus != CardFocus.OUT_OF_FOCUS
    }

    val alphaValue by animateFloatAsState(
        targetValue = if (progressIndicator.first == UIActionKeysCompose.DOC_ORG_DATA_UPDATE_DOC && progressIndicator.second) 0f else 1f,
        animationSpec = tween(
            durationMillis = 1500,
        ),
        label = "alphaValue"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (progressIndicator.first == UIActionKeysCompose.DOC_ORG_DATA_UPDATE_DOC && progressIndicator.second) 100f else 0f,
        animationSpec = tween(
            durationMillis = 500,
        ),
        label = "offsetY"
    )

    Column(
        modifier = modifier
    ) {
        if ((data.actionKey == progressIndicator.first || progressIndicator.first == UIActionKeysCompose.DOC_ORG_DATA_UPDATE_DOC) && progressIndicator.second) {
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .aspectRatio(0.7F)
                    .background(
                        color = if (progressIndicator.first == UIActionKeysCompose.DOC_ORG_DATA_UPDATE_DOC) WhiteAlpha40 else White,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                TridentLoaderMolecule()
            }
        } else {
            ConstraintLayout(
                modifier = Modifier
                    .offset(y = offsetY.dp)
                    .alpha(alphaValue)
                    .aspectRatio(0.7F)
                    .background(color = WhiteAlpha40, shape = RoundedCornerShape(24.dp))
            ) {
                val (v1, v2, v3, v4, v5, v6, v7, v8, v9) = createRefs()
                data.docHeading?.let {
                    DocHeadingOrg(
                        modifier = Modifier
                            .constrainAs(v1) {
                                top.linkTo(parent.top, margin = 20.dp)
                            },
                        data = it,
                        onUIAction = onUIAction
                    )
                }
                data.subtitleLabelMlc?.let {
                    SubtitleLabelMlc(
                        modifier = Modifier
                            .constrainAs(v4) {
                                top.linkTo(v1.bottom)
                            }
                            .padding(horizontal = 4.dp),
                        data = it
                    )
                }
                if (!data.tableBlockTwoColumns.isNullOrEmpty()) {
                    data.tableBlockTwoColumns.forEach {
                        TableBlockTwoColumnsPlainOrg(
                            modifier = Modifier.constrainAs(v2) {
                                top.linkTo(v1.bottom)
                            },
                            data = it,
                            onUIAction = onUIAction
                        )
                    }
                }
                if (!data.tableBlockOrgData.isNullOrEmpty()) {
                    data.tableBlockOrgData.forEach {
                        TableBlockPlaneOrg(
                            modifier = Modifier.constrainAs(v3) {
                                if (data.subtitleLabelMlc != null) {
                                    top.linkTo(v4.bottom)
                                } else {
                                    top.linkTo(v1.bottom)
                                }
                            },
                            data = it,
                            onUIAction = onUIAction
                        )
                    }
                }

                data.smallEmojiPanelMlcData?.let {
                    SmallEmojiPanelMlc(
                        modifier = Modifier
                            .constrainAs(v5) {
                                bottom.linkTo(v6.top, margin = 10.dp)
                                start.linkTo(v1.start, margin = 16.dp)
                                end.linkTo(parent.end, margin = 16.dp)
                            }
                            .padding(horizontal = 10.dp)
                            .background(WhiteAlpha50, shape = RoundedCornerShape(16.dp))
                            .padding(16.dp),
                        data = it,
                    )
                }

                data.tickerAtomData?.let {
                    TickerAtm(
                        modifier = Modifier
                            .constrainAs(v6) {
                                bottom.linkTo(v7.top)
                            }
                            .alpha(if (tickerDisplayed.value) 1f else 0f),
                        data = it,
                        onUIAction = onUIAction
                    )

                }

                data.docButtonHeading?.let {
                    DocButtonHeadingOrg(
                        modifier = Modifier.constrainAs(v7) {
                            bottom.linkTo(parent.bottom, margin = 28.dp)
                        },
                        data = it,
                        onUIAction = {
                            if (data.docCover == null) {
                                onUIAction(it)
                            }
                        }
                    )
                }
                data.docCover?.let {
                    DocCoverMlc(
                        modifier = Modifier
                            .constrainAs(v8) {
                                bottom.linkTo(parent.bottom)
                            }
                            .alpha(if (tickerDisplayed.value) 1f else 0f),
                        data = data.docCover,
                        onUIAction = onUIAction
                    )
                }
                if (data.docButtonHeading?.isStack == true || data.docCover?.isStack == true) {
                    Column(
                        modifier = Modifier
                            .constrainAs(v9) {
                                top.linkTo(parent.bottom)
                            }
                            .height(10.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 26.dp)
                            .background(
                                color = WhiteAlpha25,
                                shape = RoundedCornerShape(bottomEnd = 24.dp, bottomStart = 24.dp)
                            ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                    }
                }
            }
        }
    }
}

data class DocPhotoOrgData(
    val actionKey: String = UIActionKeysCompose.DOC_ORG_DATA,
    val docHeading: DocHeadingOrgData? = null,
    val tickerAtomData: TickerAtomData? = null,
    val docButtonHeading: DocButtonHeadingOrgData? = null,
    val tableBlockTwoColumns: List<TableBlockTwoColumnsPlainOrgData>? = null,
    val tableBlockOrgData: List<TableBlockPlaneOrgData>? = null,
    val docCover: DocCoverMlcData? = null,
    val subtitleLabelMlc: SubtitleLabelMlcData? = null,
    val isCurrentPage: Boolean = false,
    val smallEmojiPanelMlcData: SmallEmojiPanelMlcData? = null
) : UIElementData

@Preview
@Composable
fun DocPhotoOrgPreview() {
    val tickerAtomData = TickerAtomData(
        type = TickerType.INFORMATIVE,
        usage = TickerUsage.GRAND,
        title = "Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • "
    )
    val docButtonHeading = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
    )
    val base64String =
        "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"

    val tableBlockTwoColumns = TableBlockTwoColumnsPlainOrgData(
        id = "123",
        heading = null,
        photo = base64String,
        items = listOf(
            TableItemVerticalMlcData(
                id = "01",
                title = UiText.DynamicString("Дата\nнародження:"),
                value = UiText.DynamicString("24.08.1991")
            ),
            TableItemVerticalMlcData(
                id = "02",
                title = UiText.DynamicString("Номер:"),
                value = UiText.DynamicString("XX000000")
            ),
            TableItemVerticalMlcData(id = "03", valueAsBase64String = PreviewBase64Images.sign)
        )
    )


    val emoji = SmallEmojiPanelMlcData(
        text = UiText.DynamicString("Booster vaccine dose"),
        icon = UiIcon.DrawableResource(
            code = DiiaResourceIcon.TRIDENT.code
        )
    )

    val data = DocPhotoOrgData(
        docHeading = DocHeadingOrgData(
            heading = HeadingWithSubtitlesMlcData(
                value = "Паспорт громадянина України",
                subtitles = null
            )
        ),
        docButtonHeading = docButtonHeading,
        tickerAtomData = tickerAtomData,
        tableBlockTwoColumns = listOf(tableBlockTwoColumns),
        isCurrentPage = true,
        smallEmojiPanelMlcData = emoji
    )
    DocPhotoOrg(modifier = Modifier, data) {}
}

@Preview
@Composable
fun DocPhotoOrgWithStackPreview() {
    val tickerAtomData = TickerAtomData(
        type = TickerType.INFORMATIVE,
        usage = TickerUsage.GRAND,
        title = "Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • "
    )
    val docButtonHeading = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
        isStack = true
    )
    val base64String =
        "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"

    val tableBlockTwoColumns = TableBlockTwoColumnsPlainOrgData(
        id = "123",
        heading = null,
        photo = base64String,
        items = listOf(
            TableItemVerticalMlcData(
                id = "01",
                title = UiText.DynamicString("Дата\nнародження:"),
                value = UiText.DynamicString("24.08.1991")
            ),
            TableItemVerticalMlcData(
                id = "02",
                title = UiText.DynamicString("Номер:"),
                value = UiText.DynamicString("XX000000")
            ),
            TableItemVerticalMlcData(id = "03", valueAsBase64String = PreviewBase64Images.sign)
        )
    )

    val data = DocPhotoOrgData(
        docHeading = DocHeadingOrgData(
            heading = HeadingWithSubtitlesMlcData(
                value = "Паспорт громадянина України",
                subtitles = null
            )
        ),
        docButtonHeading = docButtonHeading,
        tickerAtomData = tickerAtomData,
        tableBlockTwoColumns = listOf(tableBlockTwoColumns),
        isCurrentPage = true
    )
    DocPhotoOrg(modifier = Modifier, data) {}
}

@Preview
@Composable
fun DocPhotoOrgWithCoverPreview() {
    val tickerAtomData = TickerAtomData(
        type = TickerType.INFORMATIVE,
        usage = TickerUsage.GRAND,
        title = "Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • "
    )
    val docButtonHeading = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
    )
    val base64String =
        "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"

    val tableBlockTwoColumns = TableBlockTwoColumnsPlainOrgData(
        id = "123",
        heading = null,
        photo = base64String,
        items = listOf(
            TableItemVerticalMlcData(
                id = "01",
                title = UiText.DynamicString("Дата\nнародження:"),
                value = UiText.DynamicString("24.08.1991")
            ),
            TableItemVerticalMlcData(
                id = "02",
                title = UiText.DynamicString("Номер:"),
                value = UiText.DynamicString("XX000000")
            ),
            TableItemVerticalMlcData(id = "03", valueAsBase64String = PreviewBase64Images.sign)
        )
    )

    val button = ButtonStrokeAdditionalAtomData(
        id = "1",
        title = UiText.DynamicString("Видалити документ"),
        interactionState = UIState.Interaction.Enabled
    )
    val docCover = DocCoverMlcData(
        title = UiText.DynamicString("Документ не знайдено \uD83D\uDE14"),
        text = UiText.DynamicString("Він відсутній у реєстрі або вже недійсний. Ви не можете користуватися документом у Дії."),
        button = button
    )

    val emoji = SmallEmojiPanelMlcData(
        text = UiText.DynamicString("Booster vaccine dose"),
        icon = UiIcon.DrawableResource(
            code = DiiaResourceIcon.TRIDENT.code
        )
    )

    val data = DocPhotoOrgData(
        docHeading = DocHeadingOrgData(
            heading = HeadingWithSubtitlesMlcData(
                value = "Паспорт громадянина України",
                subtitles = null
            )
        ),
        docButtonHeading = docButtonHeading,
        tickerAtomData = tickerAtomData,
        tableBlockTwoColumns = listOf(tableBlockTwoColumns),
        docCover = docCover,
        isCurrentPage = true,
        smallEmojiPanelMlcData = emoji
    )
    DocPhotoOrg(modifier = Modifier, data) {}
}

@Preview
@Composable
fun DocNoPhotoOrgPreview() {
    val tickerAtomData = TickerAtomData(
        type = TickerType.INFORMATIVE,
        usage = TickerUsage.GRAND,
        title = "Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • Документ оновлено о 12:06 | 22.06.2023 • "
    )
    val docButtonHeading = DocButtonHeadingOrgData(
        id = "1",
        heading = HeadingWithSubtitlesMlcData(
            value = "Name\u2028Second Name\u2028Family Name",
            subtitles = null
        ),
    )

    val tableBlockOrgData = TableBlockPlaneOrgData(
        headerMain = "Header".toDynamicString().toTableMainHeadingMlcData(),
        items = listOf(
            TableItemVerticalMlcData(
                id = "01",
                title = UiText.DynamicString("Дата\nнародження:"),
                value = UiText.DynamicString("24.08.1991")
            ),
            TableItemVerticalMlcData(
                id = "02",
                title = UiText.DynamicString("Номер:"),
                value = UiText.DynamicString("XX000000")
            ),
            TableItemVerticalMlcData(id = "03", valueAsBase64String = PreviewBase64Images.sign)
        )
    )

    val data = DocPhotoOrgData(
        docHeading = DocHeadingOrgData(
            heading = HeadingWithSubtitlesMlcData(
                value = "Картка платника податків",
                subtitles = null
            )
        ),
        subtitleLabelMlc = SubtitleLabelMlcData(UiText.DynamicString("PHOKPP")),
        docButtonHeading = docButtonHeading,
        tickerAtomData = tickerAtomData,
        tableBlockOrgData = listOf(tableBlockOrgData)
    )
    DocPhotoOrg(modifier = Modifier, data) {}
}

@Preview
@Composable
fun DocPhotoOrgLoadingPreview() {
    val data = DocPhotoOrgData(
        docHeading = null,
        docButtonHeading = null,
        tickerAtomData = null,
        tableBlockOrgData = null
    )
    DocPhotoOrg(modifier = Modifier, data, progressIndicator = Pair(data.actionKey, true)) {}

}