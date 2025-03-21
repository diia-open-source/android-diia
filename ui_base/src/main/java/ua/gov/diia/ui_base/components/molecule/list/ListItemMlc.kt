package ua.gov.diia.ui_base.components.molecule.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideSubcomposition
import com.bumptech.glide.integration.compose.RequestState
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtm
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.status.toUiModel
import ua.gov.diia.ui_base.components.atom.text.AmountAtm
import ua.gov.diia.ui_base.components.atom.text.AmountAtmData
import ua.gov.diia.ui_base.components.atom.text.Color
import ua.gov.diia.ui_base.components.atom.text.toUIModel
import ua.gov.diia.ui_base.components.conditional
import ua.gov.diia.ui_base.components.disableByInteractionState
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.noRippleClickable
import ua.gov.diia.ui_base.components.organism.list.pagination.SimplePagination
import ua.gov.diia.ui_base.components.subatomic.icon.IconBase64Subatomic
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipse23Subatomic
import ua.gov.diia.ui_base.components.subatomic.loader.LoaderCircularEclipseBlackIcon
import ua.gov.diia.ui_base.components.subatomic.preview.PreviewBase64Icons
import ua.gov.diia.ui_base.components.theme.Black
import ua.gov.diia.ui_base.components.theme.BlackAlpha30
import ua.gov.diia.ui_base.components.theme.DiiaTextStyle
import ua.gov.diia.ui_base.util.toDataActionWrapper

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ListItemMlc(
    modifier: Modifier = Modifier,
    data: ListItemMlcData,
    progressIndicator: Pair<String, Boolean> = Pair("", false),
    onUIAction: (UIAction) -> Unit
) {

    var isLoading by remember {
        mutableStateOf(
            progressIndicator.first == data.id && progressIndicator.second
        )
    }
    var isClickable by remember {
        mutableStateOf(
            data.interactionState != UIState.Interaction.Disabled || !isLoading
        )
    }

    LaunchedEffect(key1 = progressIndicator) {
        isLoading = progressIndicator.first == data.id && progressIndicator.second
    }

    LaunchedEffect(key1 = data.interactionState, key2 = isLoading) {
        isClickable =
            data.interactionState != UIState.Interaction.Disabled || !(isLoading)
    }

    val onClickData = UIAction(
        actionKey = data.actionKey,
        data = data.id,
        action = data.action
    )

    Row(
        modifier = modifier
            .conditional(!isLoading) {
                noRippleClickable(debounce = true) { onUIAction(onClickData) }
            }
            .disableByInteractionState(data.interactionState)
            .testTag(data.componentId?.asString() ?: "")
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        data.logoLeft?.let {
            when (it) {
                is UiIcon.DrawableResInt -> {
                    Image(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp),
                        painter = painterResource(
                            id = it.res
                        ),
                        contentDescription = data.iconLeftContentDescription?.asString()
                    )
                }

                is UiIcon.DrawableResource -> {
                    Image(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp),
                        painter = painterResource(
                            id = DiiaResourceIcon.getResourceId(it.code)
                        ),
                        contentDescription = data.iconLeftContentDescription?.asString()
                    )
                }

                is UiIcon.DynamicIconBase64 -> {
                    IconBase64Subatomic(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(32.dp),
                        base64Image = it.value,
                        contentDescription = data.logoLeftContentDescription?.asString()
                    )
                }

                else -> {

                }
            }
        }
        data.bigIconLeft?.let {
            Image(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp),
                painter = painterResource(
                    id = DiiaResourceIcon.getResourceId(it.code)
                ),
                contentDescription = data.iconLeftContentDescription?.asString()
            )
        }
        data.leftLogoLink?.let { link ->
            GlideSubcomposition(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(32.dp),
                model = link
            ) {
                when (state) {
                    is RequestState.Failure -> {
                        Image(
                            modifier = Modifier
                                .size(32.dp),
                            painter = painterResource(
                                id = DiiaResourceIcon.getResourceId(DiiaResourceIcon.DEFAULT_GLOBAL.code)
                            ),
                            contentDescription = data.iconLeftContentDescription?.asString(),
                            contentScale = ContentScale.Fit
                        )
                    }

                    is RequestState.Loading -> {
                        Box(
                            modifier = Modifier.size(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoaderCircularEclipseBlackIcon(
                                modifier = Modifier
                                    .size(18.dp),
                            )
                        }
                    }

                    is RequestState.Success -> {
                        Image(
                            painter,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
        AnimatedVisibility(visible = isLoading) {
            LoaderCircularEclipse23Subatomic(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .size(24.dp)
            )
        }
        AnimatedVisibility(visible = !isLoading) {
            data.iconLeft?.let {
                Image(
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .size(24.dp),
                    painter = painterResource(
                        id = DiiaResourceIcon.getResourceId(it.code)
                    ),
                    contentDescription = data.iconLeftContentDescription?.asString()
                )
            }
        }
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = data.label.asString(),
                style = DiiaTextStyle.t1BigText,
                color = Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            data.description?.let {
                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = data.description.asString(),
                    style = DiiaTextStyle.t2TextDescription,
                    color = BlackAlpha30,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            data.chipStatusAtm?.let {
                ChipStatusAtm(
                    modifier = Modifier.padding(top = 4.dp),
                    data = data.chipStatusAtm,

                )
            }
        }
        data.iconRight?.let {
            Image(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(24.dp),
                painter = painterResource(
                    id = DiiaResourceIcon.getResourceId(it.code)
                ),
                contentDescription = data.iconRightContentDescription?.asString()
            )
        }
        data.amountAtm?.let {
            AmountAtm(
                modifier = Modifier.padding(start = 8.dp),
                data = it
            )
        }
    }
}

data class ListItemMlcData(
    val actionKey: String = UIActionKeysCompose.LIST_ITEM_MLC,
    override val id: String = "",
    val label: UiText,
    val description: UiText? = null,
    val iconLeft: UiIcon.DrawableResource? = null,
    val iconLeftContentDescription: UiText? = null,
    val bigIconLeft: UiIcon.DrawableResource? = null,
    val leftLogoLink: String? = null,
    val bigIconLeftContentDescription: UiText? = null,
    val iconRight: UiIcon.DrawableResource? = null,
    val iconRightContentDescription: UiText? = null,
    val logoLeft: UiIcon? = null,
    val logoLeftContentDescription: UiText? = null,
    val action: DataActionWrapper? = null,
    val interactionState: UIState.Interaction = UIState.Interaction.Enabled,
    val componentId: UiText? = null,
    val dataJson: String? = null,
    val chipStatusAtm: ChipStatusAtmData? = null,
    val amountAtm: AmountAtmData? = null
) : UIElementData, SimplePagination

fun ListItemMlc.toUiModel(id: String? = null): ListItemMlcData {
    return ListItemMlcData(
        componentId = this.componentId?.let { UiText.DynamicString(it) },
        id = this.id ?: id ?: componentId?: UIActionKeysCompose.LIST_ITEM_MLC,
        label = label.toDynamicString(),
        description = description?.toDynamicStringOrNull(),
        iconLeft = iconLeft?.code?.let {
            UiIcon.DrawableResource(it)
        },
        bigIconLeft = bigIconLeft?.code?.let {
            UiIcon.DrawableResource(it)
        },
        leftLogoLink = leftLogoLink,
        iconRight = iconRight?.code?.let {
            UiIcon.DrawableResource(it)
        },
        logoLeft = logoLeft?.let {
//            UiIcon.DynamicIconBase64(it) //TODO Return
            UiIcon.DrawableResource(it)

        },
        action = action?.toDataActionWrapper(),
        interactionState = when (state) {
            ButtonStates.enabled.name -> UIState.Interaction.Enabled
            ButtonStates.disabled.name -> UIState.Interaction.Disabled
            ButtonStates.invisible.name -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        },
        dataJson = this.dataJson,
        chipStatusAtm = chipStatusAtm?.toUiModel(),
        amountAtm = amountAtm?.toUIModel()
    )
}

@Composable
@Preview
fun ListItemMlcPreview_Full() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        bigIconLeft = UiIcon.DrawableResource(DiiaResourceIcon.CASHBACK_CHARGE.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_Without_BigIconLeft() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_Loading() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    ListItemMlc(
        modifier = Modifier, data = state,
        progressIndicator = "123" to true
    ) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_Disabled() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Disabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_OnlyMandatoryFields() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Disabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_VeryLongTitle() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label Label Label Label Label Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        iconLeft = UiIcon.DrawableResource(DiiaResourceIcon.MENU.code),
        iconRight = UiIcon.DrawableResource(DiiaResourceIcon.ELLIPSE_ARROW_RIGHT.code),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}

@Composable
@Preview
fun ListItemMlcPreview_Chip() {
    val state = ListItemMlcData(
        id = "123",
        label = UiText.DynamicString("Label"),
        description = UiText.DynamicString("Description"),
        logoLeft = UiIcon.DynamicIconBase64(PreviewBase64Icons.apple),
        chipStatusAtm = ChipStatusAtmData(
            type = StatusChipType.PENDING, title = "STATUS"
        ),
        amountAtm = AmountAtmData(
            value = UiText.DynamicString("+3 000.00 ₴"),
            color = Color.GREEN
        ),
        action = DataActionWrapper(
            type = "type",
            subtype = "subtype",
            resource = "resource"
        ),
        interactionState = UIState.Interaction.Enabled
    )
    ListItemMlc(modifier = Modifier, data = state) {
    }
}