package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryWideAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerWithSpace
import ua.gov.diia.ui_base.components.atom.divider.DividerWithSpaceData
import ua.gov.diia.ui_base.components.atom.divider.GradientDividerAtom
import ua.gov.diia.ui_base.components.atom.divider.TableDividerAtm
import ua.gov.diia.ui_base.components.atom.divider.TableDividerAtmData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.button.SmallButtonPanelMlc
import ua.gov.diia.ui_base.components.molecule.button.SmallButtonPanelMlcData
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlc
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.CardMlc
import ua.gov.diia.ui_base.components.molecule.card.CardMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.divider.DividerLineMlc
import ua.gov.diia.ui_base.components.molecule.divider.DividerLineMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMolecule
import ua.gov.diia.ui_base.components.molecule.header.SheetNavigationBarMoleculeData
import ua.gov.diia.ui_base.components.molecule.input.InputNumberLargeMlc
import ua.gov.diia.ui_base.components.molecule.input.InputNumberLargeMlcData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlc
import ua.gov.diia.ui_base.components.molecule.list.BtnIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrg
import ua.gov.diia.ui_base.components.molecule.list.radio.RadioBtnGroupOrgData
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlcl
import ua.gov.diia.ui_base.components.molecule.list.radio.SingleChoiceMlclData
import ua.gov.diia.ui_base.components.molecule.loading.LinearLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlc
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData
import ua.gov.diia.ui_base.components.molecule.message.AttentionMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.AttentionMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.PaymentStatusMessageMolecule
import ua.gov.diia.ui_base.components.molecule.message.PaymentStatusMessageMoleculeData
import ua.gov.diia.ui_base.components.molecule.message.StatusMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StatusMessageMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.DetailsTextDescriptionMolecule
import ua.gov.diia.ui_base.components.molecule.text.DetailsTextDescriptionMoleculeData
import ua.gov.diia.ui_base.components.molecule.text.PaymentInfoOrg
import ua.gov.diia.ui_base.components.molecule.text.PaymentInfoOrgData
import ua.gov.diia.ui_base.components.molecule.text.PlainDetailsBlockMolecule
import ua.gov.diia.ui_base.components.molecule.text.PlainDetailsBlockMoleculeData
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.SubtitleLabelMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.organism.FileUploadGroupOrg
import ua.gov.diia.ui_base.components.organism.FileUploadGroupOrgData
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrg
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrgData
import ua.gov.diia.ui_base.components.organism.GroupFilesAddOrg
import ua.gov.diia.ui_base.components.organism.GroupFilesAddOrgData
import ua.gov.diia.ui_base.components.organism.MediaUploadGroupOrg
import ua.gov.diia.ui_base.components.organism.MediaUploadGroupOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrg
import ua.gov.diia.ui_base.components.organism.bottom.BottomGroupOrgData
import ua.gov.diia.ui_base.components.organism.carousel.ArticlePicCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.ArticlePicCarouselOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrg
import ua.gov.diia.ui_base.components.organism.document.TableBlockOrgData
import ua.gov.diia.ui_base.components.organism.document.TableBlockPlaneOrg
import ua.gov.diia.ui_base.components.organism.document.TableBlockPlaneOrgData
import ua.gov.diia.ui_base.components.organism.header.MediaTitleOrg
import ua.gov.diia.ui_base.components.organism.header.MediaTitleOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrg
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgData
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgDataLocal
import ua.gov.diia.ui_base.components.organism.input.QuestionFormsOrgLocal
import ua.gov.diia.ui_base.components.organism.list.CardsListOrg
import ua.gov.diia.ui_base.components.organism.list.CardsListOrgData
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundGroupOrg
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundGroupOrganism
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundGroupOrganismData
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundSeeAllOrg
import ua.gov.diia.ui_base.components.organism.list.CheckboxRoundSeeAllOrgData
import ua.gov.diia.ui_base.components.organism.list.DownloadListGroupOrganism
import ua.gov.diia.ui_base.components.organism.list.DownloadListGroupOrganismData
import ua.gov.diia.ui_base.components.organism.list.ItemListViewOrg
import ua.gov.diia.ui_base.components.organism.list.ItemListViewOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganism
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganismData
import ua.gov.diia.ui_base.components.organism.list.MultipleChoiceGroupOrganism
import ua.gov.diia.ui_base.components.organism.list.MultipleChoiceGroupOrganismDataData
import ua.gov.diia.ui_base.components.organism.list.PaginatedCardListOrg
import ua.gov.diia.ui_base.components.organism.list.PaginatedCardListOrgData
import ua.gov.diia.ui_base.components.organism.list.PlainListWithSearchOrganism
import ua.gov.diia.ui_base.components.organism.list.PlainListWithSearchOrganismData
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithAdditionalInputOrg
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithAdditionalInputOrgData
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithAltOrganism
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithAltOrganismData
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithButtonOrganism
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithButtonOrganismData
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithSearchOrganism
import ua.gov.diia.ui_base.components.organism.list.SingleChoiceWithSearchOrganismData
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationListOrganism
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationListOrganismData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrg
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.organism.sharing.SharingCodesOrg
import ua.gov.diia.ui_base.components.organism.sharing.SharingCodesOrgData
import ua.gov.diia.ui_base.components.organism.table.ContentTableOrganism
import ua.gov.diia.ui_base.components.organism.table.ContentTableOrganismData
import ua.gov.diia.ui_base.components.organism.table.TableBlockAccordionOrg
import ua.gov.diia.ui_base.components.organism.table.TableBlockAccordionOrgData
import ua.gov.diia.ui_base.components.subatomic.ticker.NoInternetTicker

@Composable
fun ColumnScope.BodyRootContainer(
    modifier: Modifier = Modifier,
    bodyViews: SnapshotStateList<UIElementData>,
    displayBlockDivider: Boolean = false,
    connectivityState: Boolean = true,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    contentLoaded: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()
    val scrollState = rememberScrollState()
    var displayBottomGradient by remember { mutableStateOf(false) }
    var displayBottomSpacer by remember { mutableStateOf(false) }
    var displayView by remember { mutableStateOf(true) }

    LaunchedEffect(
        key1 = lazyListScrollState.canScrollBackward,
        key2 = lazyListScrollState.canScrollForward
    ) {
        if (bodyViews.any { it is CardsListOrgData || it is PaginatedCardListOrgData || it is SimplePaginationListOrganismData || it is MessageListOrganismData }) {
            displayBottomGradient = displayBlockDivider && lazyListScrollState.canScrollForward
            displayView = !lazyListScrollState.canScrollBackward
            displayBottomSpacer = displayBlockDivider == false
        }
    }

    LaunchedEffect(
        key1 = scrollState.canScrollBackward,
        key2 = scrollState.canScrollForward,
    ) {
        if (bodyViews.none { it is CardsListOrgData || it is PaginatedCardListOrgData || it is SimplePaginationListOrganismData || it is MessageListOrganismData }) {
            displayBottomGradient = displayBlockDivider && scrollState.canScrollForward
            displayView = true
            displayBottomSpacer = displayBlockDivider == false
        }
    }

    Box(modifier = modifier.weight(1f)) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .conditional(!bodyViews.any {
                    it is CardsListOrgData
                            || it is PaginatedCardListOrgData
                            || it is SimplePaginationListOrganismData
                            || it is MessageListOrganismData
                            || it is DocCarouselOrgData
                            || it is FullScreenVideoOrgData
                }) {
                    verticalScroll(scrollState)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bodyViews.forEachIndexed { index, element ->
                val elementModifier = Modifier.padding(top = getTopPadding(index == 0, element))
                when (element) {
                    is SheetNavigationBarMoleculeData -> {
                        SheetNavigationBarMolecule(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is TitleLabelMlcData -> {
                        AnimatedVisibility(visible = displayView) {
                            TitleLabelMlc(
                                data = element
                            )
                        }
                    }

                    is SubtitleLabelMlcData -> {
                        AnimatedVisibility(visible = displayView) {
                            SubtitleLabelMlc(
                                modifier = modifier.padding(horizontal = 8.dp),
                                data = element
                            )
                        }
                    }

                    is PlainListWithSearchOrganismData -> {
                        PlainListWithSearchOrganism(
                            modifier = modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is TextLabelMlcData -> {
                        AnimatedVisibility(visible = displayView) {
                            TextLabelMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TextLabelContainerMlcData -> {
                        TextLabelContainerMlc(
                            data = element,
                            onUIAction = onUIAction,
                            isFirstAtBody = true
                        )
                    }

                    is ArticlePicAtmData -> {
                        ArticlePicAtm(
                            modifier = modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ArticlePicCarouselOrgData -> {
                        ArticlePicCarouselOrg(
                            modifier = modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ArticleVideoMlcData -> {
                        ArticleVideoMlc(
                            modifier = modifier,
                            data = element,
                            connectivityState = connectivityState
                        )
                    }

                    is FullScreenVideoOrgData -> {
                        AnimatedVisibility(visible = displayView) {
                            FullScreenVideoOrg(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxBtnOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            CheckboxBtnOrg(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is AttentionMessageMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            AttentionMessageMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithSearchOrganismData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SingleChoiceWithSearchOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithAltOrganismData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SingleChoiceWithAltOrganism(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithAdditionalInputOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SingleChoiceWithAdditionalInputOrg(
                                data = element,
                                onUIAction = onUIAction,
                            )
                        }
                    }

                    is MultipleChoiceGroupOrganismDataData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            MultipleChoiceGroupOrganism(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is QuestionFormsOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            QuestionFormsOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is QuestionFormsOrgDataLocal -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            QuestionFormsOrgLocal(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SearchInputV2Data -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SearchInputV2(
                                data = element,
                                focused = true,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ItemListViewOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            ItemListViewOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DownloadListGroupOrganismData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            DownloadListGroupOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ContentTableOrganismData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            ContentTableOrganism(
                                modifier = elementModifier,
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnPrimaryDefaultAtmData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            BtnPrimaryDefaultAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnStrokeDefaultAtmData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            BtnStrokeDefaultAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnPlainAtmData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            BtnPlainAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is StatusMessageMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            StatusMessageMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DetailsTextDescriptionMoleculeData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            DetailsTextDescriptionMolecule(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is PaymentInfoOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            PaymentInfoOrg(
                                data = element
                            )
                        }
                    }

                    is StubMessageMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            StubMessageMlc(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DocCarouselOrgData -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            DocCarouselOrg(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is PaymentStatusMessageMoleculeData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            PaymentStatusMessageMolecule(
                                modifier = elementModifier,
                                data = element
                            )
                        }
                    }

                    is BtnIconPlainGroupMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            BtnIconPlainGroupMlc(
                                modifier = elementModifier,
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnLoadIconPlainGroupMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            BtnLoadIconPlainGroupMlc(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is PlainDetailsBlockMoleculeData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            PlainDetailsBlockMolecule(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithButtonOrganismData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SingleChoiceWithButtonOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceMlclData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SingleChoiceMlcl(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is RadioBtnGroupOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            RadioBtnGroupOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CardMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            CardMlc(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TableBlockOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            TableBlockOrg(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TableBlockPlaneOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            TableBlockPlaneOrg(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TableBlockAccordionOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            TableBlockAccordionOrg(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxRoundGroupOrgData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            CheckboxRoundGroupOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SpacerAtmData -> {
                        SpacerAtm(data = element)
                    }

                    is TableBlockPlaneOrgData -> {
                        TableBlockPlaneOrg(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is TableDividerAtmData -> {
                        TableDividerAtm(
                            data = element
                        )
                    }

                    is DividerLineMlcData -> {
                        DividerLineMlc(
                            data = element
                        )
                    }

                    is BottomGroupOrgData -> {
                        BottomGroupOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is InputNumberLargeMlcData -> {
                        InputNumberLargeMlc(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is MediaTitleOrgData -> {
                        MediaTitleOrg(
                            modifier = modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is CheckboxRoundGroupOrganismData -> {
                        CheckboxRoundGroupOrganism(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is CheckboxRoundSeeAllOrgData -> {
                        CheckboxRoundSeeAllOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is SmallButtonPanelMlcData -> {
                        SmallButtonPanelMlc(data = element, onUIAction = onUIAction)
                    }

                    is AlertCardMlcData -> {
                        AlertCardMlc(modifier, element, onUIAction)
                    }

                    is ListItemGroupOrgData -> {
                        ListItemGroupOrg(
                            data = element,
                            onUIAction = onUIAction,
                            progressIndicator = progressIndicator
                        )
                    }

                    is MediaUploadGroupOrgData -> {
                        MediaUploadGroupOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is FileUploadGroupOrgData -> {
                        FileUploadGroupOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is GroupFilesAddOrgData -> {
                        GroupFilesAddOrg(
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is DividerWithSpaceData -> {
                        DividerWithSpace(
                            data = element
                        )
                    }
                    is SharingCodesOrgData -> {
                        SharingCodesOrg(
                            data = element,
                            onUIAction = onUIAction,
                            progressIndicator = progressIndicator
                        )
                    }
                }
            }
            bodyViews.firstOrNull { it is CardsListOrgData }?.let {
                CardsListOrg(
                    data = it as CardsListOrgData,
                    lazyListState = lazyListScrollState,
                    onUIAction = onUIAction
                )
            }
            bodyViews.firstOrNull { it is MessageListOrganismData }?.let {
                MessageListOrganism(
                    modifier = Modifier.conditional(!lazyListScrollState.canScrollBackward) {
                        this
                    },
                    data = it as MessageListOrganismData,
                    onUIAction = onUIAction
                )
            }
            bodyViews.firstOrNull { it is PaginatedCardListOrgData }?.let {
                PaginatedCardListOrg(
                    data = it as PaginatedCardListOrgData,
                    lazyListState = lazyListScrollState,
                    onUIAction = onUIAction
                )
            }
            bodyViews.firstOrNull { it is SimplePaginationListOrganismData }?.let {
                SimplePaginationListOrganism(
                    modifier = Modifier.fillMaxSize(),
                    data = it as SimplePaginationListOrganismData,
                    lazyListState = lazyListScrollState,
                    onUIAction = onUIAction
                )
            }
            addBottomPadding(displayBottomSpacer)
        }
        GradientDividerContentBlock(displayBottomGradient)
        LinearLoaderContentBlock(contentLoaded, onUIAction)
    }
}

@Composable
fun BoxScope.NoInternetBlock(internetAvailable: Boolean) {
    if (!internetAvailable) {
        NoInternetTicker(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun BoxScope.GradientDividerContentBlock(displayBottomGradient: Boolean) {
    if (displayBottomGradient && !WindowInsets.isImeVisible) {
        GradientDividerAtom(
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LinearLoaderContentBlock(
    contentLoaded: Pair<String, Boolean>,
    onUIAction: (UIAction) -> Unit
) {
    if (!contentLoaded.second) {
        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL) {
            Column(modifier = Modifier.fillMaxWidth()) {
                TopGroupOrg(
                    data = TopGroupOrgData(
                        navigationPanelMlcData = NavigationPanelMlcData(
                            title = UiText.DynamicString(""),
                            isContextMenuExist = false
                        )
                    ), onUIAction = onUIAction
                )
                LinearLoadingMolecule(
                    labelDisplayed = true
                )
            }

        }
        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_LINEAR_PAGINATION) {
            LinearLoadingMolecule(
                labelDisplayed = false
            )
        }
        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_LINEAR_WITHOUT_BACK_BTN_WITH_LABEL) {
            Column(modifier = Modifier) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearLoadingMolecule(
                    labelDisplayed = true
                )
            }
        }
    }
}

private fun getTopPadding(isFirst: Boolean, element: UIElementData): Dp {
    if (isFirst) return 0.dp
    return when (element) {
        is BtnPlainAtmData -> 8.dp
        is DownloadListGroupOrganismData -> 12.dp
        else -> 24.dp
    }
}

@Composable
private fun addBottomPadding(displayBottomSpacer: Boolean) {
    if (displayBottomSpacer) {
        Box(modifier = Modifier) {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun BodyRootContainer_HelloScreenPreview() {
    val _stub = remember { mutableStateListOf<UIElementData>() }
    val stub: SnapshotStateList<UIElementData> = _stub
    _stub.apply {
        addAll(
            listOf(
                TitleLabelMlcData(label = "Вітаємо, Надія \uD83D\uDC4B"),
                TextWithParametersData(
                    text = UiText.DynamicString("Користувачі, котрі мали рахунки у ліквідованих банках можуть подати заяву до Фонду Гарантування Вкладів Фізичних Осіб на отримання компенсації по своїм втраченим вкладам. Виплата компенсації здійснюватиметься на картку єПідтримка.\n\nЩоб подати заяву потрібно:\n\n• обрати зі списку ліквідованих банків той, в якому ви мали рахунок і вклад;"),
                    parameters = emptyList()
                ),
                CheckboxBtnOrgData(
                    id = "",
                    options = SnapshotStateList<CheckboxSquareMlcData>().apply {
                        add(
                            CheckboxSquareMlcData(
                                title = UiText.DynamicString("Надаю дозвіл на обробку моїх банківських даних."),
                                interactionState = UIState.Interaction.Enabled,
                                selectionState = UIState.Selection.Unselected
                            )
                        )
                    },
                    buttonData = BtnPrimaryDefaultAtmData(
                        id = "",
                        title = UiText.DynamicString("Далі"),
                        interactionState = UIState.Interaction.Disabled
                    )
                ),
                SearchInputV2Data(),
                BtnPrimaryWideAtmData(
                    title = UiText.DynamicString("Label"),
                    id = "id",
                    interactionState = UIState.Interaction.Enabled
                )
            )
        )
    }

    Column {
        BodyRootContainer(
            bodyViews = stub
        ) {

        }
    }
}

@Preview
@Composable
fun BodyRootContainer_Test() {
    val _stub = remember { mutableStateListOf<UIElementData>() }
    val stub: SnapshotStateList<UIElementData> = _stub
    _stub.apply {
        addAll(
            listOf(
                TitleLabelMlcData(label = "Вітаємо, Надія \uD83D\uDC4B"),
                TextWithParametersData(
                    text = UiText.DynamicString("Користувачі, котрі мали рахунки у ліквідованих банках можуть подати заяву до Фонду Гарантування Вкладів Фізичних Осіб на отримання компенсації по своїм втраченим вкладам. Виплата компенсації здійснюватиметься на картку єПідтримка.\n\nЩоб подати заяву потрібно:\n\n• обрати зі списку ліквідованих банків той, в якому ви мали рахунок і вклад;"),
                    parameters = emptyList()
                ),
                TestListData(items = LoremIpsum(30).values.joinToString().split(" "))
            )
        )
    }

    Column {
        BodyRootContainer(
            bodyViews = stub
        ) {

        }
    }
}

@Preview
@Composable
fun BodyRootContainer_LinearLoading() {
    val _stub = remember { mutableStateListOf<UIElementData>() }
    val stub: SnapshotStateList<UIElementData> = _stub
    val contentLoaded = Pair(UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL, false)

    Column {
        BodyRootContainer(
            bodyViews = stub,
            contentLoaded = contentLoaded
        ) {

        }
    }
}


data class TestListData(val items: List<String>) : UIElementData
