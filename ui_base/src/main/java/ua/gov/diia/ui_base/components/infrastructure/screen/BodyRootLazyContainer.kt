package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnStrokeDefaultAtmData
import ua.gov.diia.ui_base.components.atom.divider.DividerWithSpace
import ua.gov.diia.ui_base.components.atom.divider.DividerWithSpaceData
import ua.gov.diia.ui_base.components.atom.divider.TableDividerAtm
import ua.gov.diia.ui_base.components.atom.divider.TableDividerAtmData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.loadItem
import ua.gov.diia.ui_base.components.molecule.button.SmallButtonPanelMlc
import ua.gov.diia.ui_base.components.molecule.button.SmallButtonPanelMlcData
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlc
import ua.gov.diia.ui_base.components.molecule.card.AlertCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.CardMlc
import ua.gov.diia.ui_base.components.molecule.card.CardMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrg
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
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
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrg
import ua.gov.diia.ui_base.components.organism.FullScreenVideoOrgData
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
import ua.gov.diia.ui_base.components.organism.list.MultipleChoiceGroupOrganism
import ua.gov.diia.ui_base.components.organism.list.MultipleChoiceGroupOrganismDataData
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
import ua.gov.diia.ui_base.components.organism.list.loadItems
import ua.gov.diia.ui_base.components.organism.list.loadPaginatedCardListOrg
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationCard
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePaginationListOrganismData
import ua.gov.diia.ui_base.components.organism.list.pagination.loadSimplePaginationListOrg
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrg
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.organism.sharing.SharingCodesOrg
import ua.gov.diia.ui_base.components.organism.sharing.SharingCodesOrgData
import ua.gov.diia.ui_base.components.organism.table.ContentTableOrganism
import ua.gov.diia.ui_base.components.organism.table.ContentTableOrganismData

@Composable
fun ColumnScope.BodyRootLazyContainer(
    modifier: Modifier = Modifier,
    bodyViews: SnapshotStateList<UIElementData>,
    displayBlockDivider: Boolean = false,
    connectivityState: Boolean = true,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    contentLoaded: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()
    var displayBottomGradient by remember { mutableStateOf(false) }

    LaunchedEffect(
        key1 = lazyListScrollState.canScrollBackward,
        key2 = lazyListScrollState.canScrollForward
    ) {
        displayBottomGradient = displayBlockDivider && lazyListScrollState.canScrollForward
    }

    val paginationItems = bodyViews.firstOrNull {
        it is PaginatedCardListOrgData || it is SimplePaginationListOrganismData
    }?.let {
        return@let when (it) {
            is PaginatedCardListOrgData -> it.items.collectAsLazyPagingItems()
            is SimplePaginationListOrganismData -> it.items.collectAsLazyPagingItems()
            else -> null
        }
    }

    Box(
        modifier = modifier.weight(1f),
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            state = lazyListScrollState,
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            bodyViews.forEachIndexed { index, element ->
                val elementModifier =
                    Modifier.padding(top = getTopPadding(index == 0, element))
                when (element) {
                    is SheetNavigationBarMoleculeData -> {
                        loadItem(contentType = SheetNavigationBarMoleculeData::class) {
                            SheetNavigationBarMolecule(
                                modifier = Modifier.padding(horizontal = 24.dp),
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TitleLabelMlcData -> {
                        loadItem(TitleLabelMlcData::class) {
                            TitleLabelMlc(
                                data = element
                            )
                        }
                    }

                    is SubtitleLabelMlcData -> {
                        loadItem(SubtitleLabelMlcData::class) {
                            SubtitleLabelMlc(
                                modifier = modifier.padding(horizontal = 8.dp),
                                data = element
                            )
                        }
                    }

                    is PlainListWithSearchOrganismData -> {
                        loadItem(PlainListWithSearchOrganismData::class) {
                            PlainListWithSearchOrganism(
                                modifier = modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TextLabelMlcData -> {
                        loadItem(TextLabelMlcData::class) {
                            TextLabelMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TextLabelContainerMlcData -> {
                        loadItem(TextLabelContainerMlcData::class) {
                            TextLabelContainerMlc(
                                data = element,
                                onUIAction = onUIAction,
                                isFirstAtBody = true
                            )
                        }
                    }

                    is ArticlePicAtmData -> {
                        loadItem(ArticlePicAtmData::class) {
                            ArticlePicAtm(
                                modifier = modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ArticlePicCarouselOrgData -> {
                        loadItem(ArticlePicCarouselOrgData::class) {
                            ArticlePicCarouselOrg(
                                modifier = modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ArticleVideoMlcData -> {
                        loadItem(ArticleVideoMlcData::class) {
                            ArticleVideoMlc(
                                modifier = modifier,
                                data = element,
                                connectivityState = connectivityState
                            )
                        }
                    }

                    is FullScreenVideoOrgData -> {
                        loadItem(FullScreenVideoOrgData::class) {
                            FullScreenVideoOrg(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxBtnOrgData -> {
                        loadItem(CheckboxBtnOrgData::class) {
                            CheckboxBtnOrg(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is AttentionMessageMlcData -> {
                        loadItem(AttentionMessageMlcData::class) {
                            AttentionMessageMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithSearchOrganismData -> {
                        loadItem(SingleChoiceWithSearchOrganismData::class) {
                            SingleChoiceWithSearchOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithAltOrganismData -> {
                        loadItem(SingleChoiceWithAltOrganismData::class) {
                            SingleChoiceWithAltOrganism(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithAdditionalInputOrgData -> {
                        loadItem(SingleChoiceWithAdditionalInputOrgData::class) {
                            SingleChoiceWithAdditionalInputOrg(
                                data = element,
                                onUIAction = onUIAction,
                            )
                        }
                    }

                    is MultipleChoiceGroupOrganismDataData -> {
                        loadItem(MultipleChoiceGroupOrganismDataData::class) {
                            MultipleChoiceGroupOrganism(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is QuestionFormsOrgData -> {
                        loadItem(QuestionFormsOrgData::class) {
                            QuestionFormsOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is QuestionFormsOrgDataLocal -> {
                        loadItem(QuestionFormsOrgDataLocal::class) {
                            QuestionFormsOrgLocal(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SearchInputV2Data -> {
                        loadItem(SearchInputV2Data::class) {
                            SearchInputV2(
                                data = element,
                                focused = true,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ItemListViewOrgData -> {
                        loadItem(ItemListViewOrgData::class) {
                            ItemListViewOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DownloadListGroupOrganismData -> {
                        loadItem(DownloadListGroupOrganismData::class) {
                            DownloadListGroupOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ContentTableOrganismData -> {
                        loadItem(ContentTableOrganismData::class) {
                            ContentTableOrganism(
                                modifier = elementModifier,
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnPrimaryDefaultAtmData -> {
                        loadItem(BtnPrimaryDefaultAtmData::class) {
                            BtnPrimaryDefaultAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnStrokeDefaultAtmData -> {
                        loadItem(BtnPrimaryDefaultAtmData::class) {
                            BtnStrokeDefaultAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnPlainAtmData -> {
                        loadItem(BtnPlainAtmData::class) {
                            BtnPlainAtm(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is StatusMessageMlcData -> {
                        loadItem(StatusMessageMlcData::class) {
                            StatusMessageMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DetailsTextDescriptionMoleculeData -> {
                        loadItem(DetailsTextDescriptionMoleculeData::class) {
                            DetailsTextDescriptionMolecule(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is PaymentInfoOrgData -> {
                        loadItem(PaymentInfoOrgData::class) {
                            PaymentInfoOrg(
                                data = element
                            )
                        }
                    }

                    is StubMessageMlcData -> {
                        loadItem(StubMessageMlcData::class) {
                            StubMessageMlc(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is DocCarouselOrgData -> {
                        loadItem(DocCarouselOrgData::class) {
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
                    }

                    is PaymentStatusMessageMoleculeData -> {
                        loadItem(PaymentStatusMessageMoleculeData::class) {
                            PaymentStatusMessageMolecule(
                                modifier = elementModifier,
                                data = element
                            )
                        }
                    }

                    is BtnIconPlainGroupMlcData -> {
                        loadItem(BtnIconPlainGroupMlcData::class) {
                            BtnIconPlainGroupMlc(
                                modifier = elementModifier,
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is PlainDetailsBlockMoleculeData -> {
                        loadItem(PlainDetailsBlockMoleculeData::class) {
                            PlainDetailsBlockMolecule(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceWithButtonOrganismData -> {
                        loadItem(SingleChoiceWithButtonOrganismData::class) {
                            SingleChoiceWithButtonOrganism(
                                modifier = elementModifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SingleChoiceMlclData -> {
                        loadItem(SingleChoiceMlclData::class) {
                            SingleChoiceMlcl(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is RadioBtnGroupOrgData -> {
                        loadItem(RadioBtnGroupOrgData::class) {
                            RadioBtnGroupOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CardMlcData -> {
                        loadItem(CardMlcData::class) {
                            CardMlc(
                                data = element,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TableBlockOrgData -> {
                        loadItem(TableBlockOrgData::class) {
                            TableBlockOrg(
                                modifier = Modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxRoundGroupOrgData -> {
                        loadItem(CheckboxRoundGroupOrgData::class) {
                            CheckboxRoundGroupOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SpacerAtmData -> {
                        loadItem(SpacerAtmData::class) {
                            SpacerAtm(data = element)
                        }
                    }

                    is TableBlockPlaneOrgData -> {
                        loadItem(TableBlockPlaneOrgData::class) {
                            TableBlockPlaneOrg(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is TableDividerAtmData -> {
                        loadItem(TableDividerAtmData::class) {
                            TableDividerAtm(
                                data = element
                            )
                        }
                    }

                    is BottomGroupOrgData -> {
                        loadItem(BottomGroupOrgData::class) {
                            BottomGroupOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is InputNumberLargeMlcData -> {
                        loadItem(InputNumberLargeMlcData::class) {
                            InputNumberLargeMlc(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is MediaTitleOrgData -> {
                        loadItem(MediaTitleOrgData::class) {
                            MediaTitleOrg(
                                modifier = modifier,
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxRoundGroupOrganismData -> {
                        loadItem(CheckboxRoundGroupOrganismData::class) {
                            CheckboxRoundGroupOrganism(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CheckboxRoundSeeAllOrgData -> {
                        loadItem(CheckboxRoundSeeAllOrgData::class) {
                            CheckboxRoundSeeAllOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SmallButtonPanelMlcData -> {
                        loadItem(SmallButtonPanelMlcData::class) {
                            SmallButtonPanelMlc(data = element, onUIAction = onUIAction)
                        }
                    }

                    is AlertCardMlcData -> {
                        loadItem(AlertCardMlcData::class) {
                            AlertCardMlc(modifier, element, onUIAction)
                        }
                    }

                    is ListItemGroupOrgData -> {
                        loadItem(ListItemGroupOrgData::class) {
                            ListItemGroupOrg(
                                data = element,
                                onUIAction = onUIAction,
                                progressIndicator = progressIndicator
                            )
                        }
                    }

                    is DividerWithSpaceData -> {
                        loadItem(DividerWithSpaceData::class) {
                            DividerWithSpace(
                                data = element
                            )
                        }
                    }

                    is SharingCodesOrgData -> {
                        loadItem(SharingCodesOrgData::class) {
                            SharingCodesOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is CardsListOrgData -> {
                        loadItems(
                            item = element,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
                    }

                    is PaginatedCardListOrgData -> {
                        paginationItems?.let {
                            loadPaginatedCardListOrg(
                                items = paginationItems as LazyPagingItems<CardMlcData>,
                                progressIndicator = progressIndicator,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SimplePaginationListOrganismData -> {
                        paginationItems?.let {
                            loadSimplePaginationListOrg(
                                modifier = Modifier,
                                items = paginationItems as LazyPagingItems<SimplePaginationCard>,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SharingCodesOrgData -> {
                        loadItem(SharingCodesOrgData::class) {
                            SharingCodesOrg(
                                data = element,
                                onUIAction = onUIAction,
                                progressIndicator = progressIndicator
                            )
                        }
                    }
                }
            }
        }
        GradientDividerContentBlock(displayBottomGradient)
        LinearLoaderContentBlock(contentLoaded, onUIAction)
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
    }
}