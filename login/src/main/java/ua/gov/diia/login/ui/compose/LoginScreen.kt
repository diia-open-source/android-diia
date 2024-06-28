package ua.gov.diia.login.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.paint
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.login.R
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBorderedMlc
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBorderedMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.loading.FullScreenLoadingMolecule
import ua.gov.diia.ui_base.components.molecule.loading.TridentLoaderMolecule
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlc
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrg
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.provideTestTagsAsResourceId
import ua.gov.diia.ui_base.components.subatomic.loader.TridentLoaderWithUIBlocking
import ua.gov.diia.ui_base.components.theme.BlackAlpha30

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    toolbar: SnapshotStateList<UIElementData>,
    body: SnapshotStateList<UIElementData>,
    bottom: SnapshotStateList<UIElementData>,
    screenNavigationProcessing: State<Boolean>,
    isLoading: Pair<String, Boolean>,
    onEvent: (UIAction) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().provideTestTagsAsResourceId()) {
        Column(
            modifier = modifier
                .paint(
                    painterResource(id = R.drawable.bg_blue_yellow_gradient),
                    contentScale = ContentScale.FillBounds
                )
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            if (screenNavigationProcessing.value) {
                FullScreenLoadingMolecule(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {}
                        )
                        .fillMaxSize()
                        .testTag(stringResource(R.string.login_screen_loading_test_tag)),
                    backgroundColor = BlackAlpha30
                )
            }
            Column {
                toolbar.forEach { item ->
                    when (item) {
                        is TopGroupOrgData -> {
                            TopGroupOrg(
                                data = item,
                                onUIAction = onEvent
                            )
                        }
                    }
                }
            }

            val scrollState = rememberScrollState()
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .verticalScroll(scrollState),
            ) {
                val listVisible = remember { mutableStateOf(false) }
                body.forEach { element ->
                    when (element) {
                        is TextLabelMlcData -> {
                            TextLabelMlc(
                                modifier = Modifier.testTag(stringResource(R.string.login_screen_text_test_tag)),
                                data = element,
                                onUIAction = onEvent
                            )
                        }

                        is CheckboxBorderedMlcData -> {
                            CheckboxBorderedMlc(
                                data = element,
                                onUIAction = onEvent
                            )
                        }

                        is ListItemGroupOrgData -> {
                            ListItemGroupOrg(
                                data = element,
                                onUIAction = onEvent,
                                modifier = modifier
                                    .fillMaxWidth()
                                    .wrapContentSize()
                                    .onGloballyPositioned {
                                        //making list visible only after it was positioned
                                        if (!listVisible.value) {
                                            listVisible.value = true
                                        }
                                    }
                                    .alpha(if (listVisible.value) 1f else 0f)
                            )
                        }
                    }
                }
            }
        }
        if (isLoading.first == UIActionKeysCompose.PAGE_LOADING_TRIDENT && isLoading.second) {
            TridentLoaderMolecule()
        }
        TridentLoaderWithUIBlocking(contentLoaded = isLoading.first to !isLoading.second)
    }
}

@Preview
@Composable
fun LoginScreenPreview(
) {
    val _toolbarData = remember { mutableStateListOf<UIElementData>() }
    val _bodyData = remember { mutableStateListOf<UIElementData>() }
    val _bottomData = remember { mutableStateListOf<UIElementData>() }

    _toolbarData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.DynamicString("Авторизація"),
                label = UiText.DynamicString("Версія Дії: 3.0.78.1722")
            )
        )
    )

    val linkText =
        UiText.DynamicString("Увійдіть за допомогою BankID Національного банку України, використовуючи свій інтернет-банкінг, або за допомогою NFC-чипу у біометричних документах.\n\nДля авторизації ознайомтесь зі змістом.\n{details}")
    val linkParameter = TextParameter(
        type = "link",
        data = TextParameter.Data(
            name = UiText.DynamicString("details"),
            alt = UiText.DynamicString("Повідомлення про обробку персональних даних"),
            resource = UiText.DynamicString("https://diia.gov.ua/app_policy"),
        )
    )
    _bodyData.add(
        TextLabelMlcData(
            actionKey = "ACTION_NAVIGATE_TO_POLICY",
            text = linkText,
            parameters = listOf(linkParameter)
        )
    )

    _bodyData.add(
        CheckboxBorderedMlcData(
            actionKey = "ACTION_CHECKBOX_CHECKED",
            data = CheckboxSquareMlcData(
                actionKey = "LoginConst.ACTION_CHECKBOX_CHECKED",
                title = UiText.DynamicString("Я ознайомився зі змістом Повідомлення про обробку персональних даних."),
                interactionState = UIState.Interaction.Enabled,
                selectionState = UIState.Selection.Unselected
            )
        )
    )
    val list = SnapshotStateList<ListItemMlcData>().also {
        for (i in 1..4) {
            it.add(
                ListItemMlcData(
                    id = i.toString(),
                    label = UiText.DynamicString("BankID $i"),
                    interactionState = UIState.Interaction.Disabled
                )
            )
        }
    }
    _bodyData.add(
        ListItemGroupOrgData(
            title = UiText.StringResource(R.string.login_screen_bank_list_title),
            itemsList = list
        )
    )


    val navState = remember { mutableStateOf(value = false) }
    LoginScreen(
        toolbar = _toolbarData,
        body = _bodyData,
        bottom = _bottomData,
        screenNavigationProcessing = navState,
        isLoading = "" to false,
    ) {}
}