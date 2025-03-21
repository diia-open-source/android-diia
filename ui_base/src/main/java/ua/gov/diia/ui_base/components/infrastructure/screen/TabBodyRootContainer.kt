package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
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
import androidx.compose.ui.unit.dp
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPlainAtmData
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtm
import ua.gov.diia.ui_base.components.atom.button.BtnPrimaryDefaultAtmData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtm
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtm
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.text.SectionTitleAtm
import ua.gov.diia.ui_base.components.atom.text.SectionTitleAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersAtom
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.ContainerType
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.card.BlackCardMlc
import ua.gov.diia.ui_base.components.molecule.card.BlackCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlc
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.LoopingVideoPlayerCardMlc
import ua.gov.diia.ui_base.components.molecule.card.LoopingVideoPlayerCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.WhiteCardMlc
import ua.gov.diia.ui_base.components.molecule.card.WhiteCardMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBtnOrgData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrg
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.loading.LinearLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlc
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlc
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrg
import ua.gov.diia.ui_base.components.molecule.tile.ServiceCardTileOrgData
import ua.gov.diia.ui_base.components.organism.bottom.BtnIconRoundedGroupOrg
import ua.gov.diia.ui_base.components.organism.bottom.BtnIconRoundedGroupOrgData
import ua.gov.diia.ui_base.components.organism.carousel.HalvedCardCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.HalvedCardCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.ImageCardCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.ImageCardCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.SmallNotificationCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.SmallNotificationCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.VerticalCardCarouselOrg
import ua.gov.diia.ui_base.components.organism.carousel.VerticalCardCarouselOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganism
import ua.gov.diia.ui_base.components.organism.list.MessageListOrganismData
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrg
import ua.gov.diia.ui_base.components.organism.pager.DocCarouselOrgData
import ua.gov.diia.ui_base.components.subatomic.ticker.NoInternetTicker


@Composable
fun ColumnScope.TabBodyRootContainer(
    modifier: Modifier = Modifier,
    bodyViews: SnapshotStateList<UIElementData>,
    displayBlockDivider: Boolean = false,
    connectivityState: Boolean = true,
    containerType: ContainerType = ContainerType.SERVICE,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    contentLoaded: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {
    val lazyListScrollState = rememberLazyListState()
    val lazyGridState: LazyGridState = rememberLazyGridState()

    val scrollState = rememberScrollState()
    var displayBottomGradient by remember { mutableStateOf(false) }
    var displayView by remember { mutableStateOf(true) }

    LaunchedEffect(
        key1 = lazyListScrollState.canScrollBackward,
        key2 = lazyListScrollState.canScrollForward
    ) {
        if (bodyViews.any { it is MessageListOrganismData }) {
            displayBottomGradient = displayBlockDivider && lazyListScrollState.canScrollForward
            displayView = !lazyListScrollState.canScrollBackward
        }
    }

    LaunchedEffect(
        key1 = lazyGridState.canScrollBackward,
        key2 = lazyGridState.canScrollForward
    ) {
        if (bodyViews.any { it is ServiceCardTileOrgData }) {
            displayBottomGradient = displayBlockDivider && lazyGridState.canScrollForward
            displayView = !lazyGridState.canScrollBackward
        }
    }

    LaunchedEffect(
        key1 = scrollState.canScrollBackward,
        key2 = scrollState.canScrollForward,
    ) {
        if (bodyViews.none { it is MessageListOrganismData }) {
            displayBottomGradient = displayBlockDivider && scrollState.canScrollForward
            displayView = true
        }
        if (bodyViews.none { it is ServiceCardTileOrgData }) {
            displayBottomGradient = displayBlockDivider && scrollState.canScrollForward
            displayView = true
        }
    }

    Box(
        modifier = modifier.weight(1f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .conditional(!bodyViews.any { it is ServiceCardTileOrgData || it is MessageListOrganismData || it is DocCarouselOrgData }) {
                    verticalScroll(scrollState)
                },
            horizontalAlignment = Alignment.Start
        ) {
            bodyViews.forEachIndexed { index, element ->
                when (element) {
                    is TextLabelMlcData -> {
                        TextLabelMlc(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is SectionTitleAtmData -> {
                        SectionTitleAtm(
                            modifier = Modifier,
                            isFirstAtBody = index == 0,
                            data = element
                        )
                    }

                    is SmallNotificationCarouselOrgData -> {
                        SmallNotificationCarouselOrg(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is BlackCardMlcData -> {
                        BlackCardMlc(
                            modifier = Modifier,
                            data = element,
                            isFirstAtBody = index == 0,
                            onUIAction = onUIAction
                        )
                    }

                    is WhiteCardMlcData -> {
                        WhiteCardMlc(
                            modifier = Modifier,
                            data = element,
                            isFirstAtBody = index == 0,
                            onUIAction = onUIAction
                        )
                    }

                    is BtnIconRoundedGroupOrgData -> {
                        BtnIconRoundedGroupOrg(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ImageCardMlcData -> {
                        ImageCardMlc(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is LoopingVideoPlayerCardMlcData -> {
                        LoopingVideoPlayerCardMlc(
                            modifier = Modifier,
                            data = element,
                            connectivityState = connectivityState,
                            onUIAction = onUIAction
                        )
                    }

                    is HalvedCardCarouselOrgData -> {
                        HalvedCardCarouselOrg(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ImageCardCarouselOrgData -> {
                        ImageCardCarouselOrg(
                            modifier = modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is ListItemGroupOrgData -> {
                        ListItemGroupOrg(
                            modifier = Modifier,
                            data = element,
                            onUIAction = onUIAction
                        )
                    }

                    is VerticalCardCarouselOrgData -> {
                        VerticalCardCarouselOrg(
                            modifier = Modifier,
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

                    is TextLabelContainerMlcData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            TextLabelContainerMlc(
                                data = element,
                                isFirstAtBody = index == 0,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ArticlePicAtmData -> {
                        AnimatedVisibility(visible = displayView) {
                            ArticlePicAtm(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ArticleVideoMlcData -> {
                        AnimatedVisibility(visible = displayView) {
                            ArticleVideoMlc(
                                data = element,
                                connectivityState = connectivityState,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SearchInputV2Data -> {
                        AnimatedVisibility(visible = displayView) {
                            SearchInputV2(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is ChipTabsOrgData -> {
                        AnimatedVisibility(visible = displayView) {
                            ChipTabsOrg(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is BtnPrimaryDefaultAtmData -> {
                        BtnPrimaryDefaultAtm(
                            modifier = modifier.align(Alignment.CenterHorizontally),
                            data = element,
                            progressIndicator = progressIndicator,
                            onUIAction = onUIAction
                        )
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

                    is TextWithParametersData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            TextWithParametersAtom(
                                data = element,
                                onUIAction = onUIAction
                            )
                        }
                    }

                    is SpacerAtmData -> {
                        AnimatedVisibility(visible = !lazyListScrollState.canScrollBackward) {
                            SpacerAtm(
                                data = element,
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
                }
            }

            bodyViews.firstOrNull { it is MessageListOrganismData }?.let {
                MessageListOrganism(
                    modifier = Modifier.conditional(!lazyListScrollState.canScrollBackward) {
                        padding(top = 24.dp)
                    },
                    data = it as MessageListOrganismData,
                    onUIAction = onUIAction
                )
            }
            bodyViews.firstOrNull { it is ServiceCardTileOrgData }?.let {
                ServiceCardTileOrg(
                    lazyGridState = lazyGridState,
                    data = it as ServiceCardTileOrgData,
                    onUIAction = onUIAction,
                    modifier = Modifier
                )
            }
        }
        NoInternetBlock(connectivityState)
        GradientDividerContentBlock(displayBottomGradient, containerType)
        LinearLoaderContentBlock(contentLoaded)
    }
}

@Composable
fun BoxScope.LinearLoaderContentBlock(contentLoaded: Pair<String, Boolean>) {
    if (!contentLoaded.second) {
        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_LINEAR_WITH_LABEL) {
            LinearLoadingMolecule(
                modifier = Modifier.Companion.align(alignment = Alignment.TopCenter),
                labelDisplayed = true
            )
        }
        if (contentLoaded.first == UIActionKeysCompose.PAGE_LOADING_LINEAR_PAGINATION) {
            LinearLoadingMolecule(
                modifier = Modifier.Companion.align(alignment = Alignment.TopCenter),
                labelDisplayed = false
            )
        }
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

@Preview
@Composable
fun TabBodyRootContainer_HelloScreenPreview() {
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
            )
        )
    }

    Column {
        TabBodyRootContainer(
            bodyViews = stub
        ) {

        }
    }
}