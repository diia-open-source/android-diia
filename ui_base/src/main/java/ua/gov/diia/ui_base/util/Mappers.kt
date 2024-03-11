package ua.gov.diia.ui_base.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.core.models.common_compose.atm.button.BtnPlainIconAtm
import ua.gov.diia.core.models.common_compose.atm.chip.ChipStatusAtm
import ua.gov.diia.core.models.common_compose.atm.chip.Type
import ua.gov.diia.core.models.common_compose.atm.icon.BadgeCounterAtm
import ua.gov.diia.core.models.common_compose.atm.icon.DoubleIconAtm
import ua.gov.diia.core.models.common_compose.atm.icon.IconAtm
import ua.gov.diia.core.models.common_compose.atm.icon.SmallIconAtm
import ua.gov.diia.core.models.common_compose.atm.media.ArticlePicAtm
import ua.gov.diia.core.models.common_compose.atm.text.SectionTitleAtm
import ua.gov.diia.core.models.common_compose.atm.text.TickerAtm
import ua.gov.diia.core.models.common_compose.general.Action
import ua.gov.diia.core.models.common_compose.general.ButtonStates
import ua.gov.diia.core.models.common_compose.mlc.button.BtnIconRoundedMlc
import ua.gov.diia.core.models.common_compose.mlc.card.HalvedCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.IconCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.ImageCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.SmallNotificationMlc
import ua.gov.diia.core.models.common_compose.mlc.card.VerticalCardMlc
import ua.gov.diia.core.models.common_compose.mlc.card.WhiteCardMlc
import ua.gov.diia.core.models.common_compose.mlc.header.TitleGroupMlc
import ua.gov.diia.core.models.common_compose.mlc.list.ListItemMlc
import ua.gov.diia.core.models.common_compose.mlc.media.ArticleVideoMlc
import ua.gov.diia.core.models.common_compose.mlc.text.TextLabelContainerMlc
import ua.gov.diia.core.models.common_compose.org.button.BtnIconRoundedGroupOrg
import ua.gov.diia.core.models.common_compose.org.carousel.ArticlePicCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.HalvedCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.SmallNotificationCarouselOrg
import ua.gov.diia.core.models.common_compose.org.carousel.VerticalCardCarouselOrg
import ua.gov.diia.core.models.common_compose.org.header.ChipTabsOrg
import ua.gov.diia.core.models.common_compose.org.header.MediaTitleOrg
import ua.gov.diia.core.models.common_compose.org.header.NavigationPanelMlc
import ua.gov.diia.core.models.common_compose.org.header.TopGroupOrg
import ua.gov.diia.core.models.common_compose.org.list.ListItemGroupOrg
import ua.gov.diia.core.models.common_compose.table.HeadingWithSubtitlesMlc
import ua.gov.diia.ui_base.components.atom.button.BtnPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.BadgeCounterAtmData
import ua.gov.diia.ui_base.components.atom.icon.DoubleIconAtmData
import ua.gov.diia.ui_base.components.atom.icon.IconAtmData
import ua.gov.diia.ui_base.components.atom.icon.SmallIconAtmData
import ua.gov.diia.ui_base.components.atom.media.ArticlePicAtmData
import ua.gov.diia.ui_base.components.atom.status.ChipStatusAtmData
import ua.gov.diia.ui_base.components.atom.status.StatusChipType
import ua.gov.diia.ui_base.components.atom.text.SectionTitleAtmData
import ua.gov.diia.ui_base.components.atom.text.TickerAtomData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDrawableResource
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDrawableResourceOrNull
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicStringOrNull
import ua.gov.diia.ui_base.components.molecule.button.BtnIconRoundedMlcData
import ua.gov.diia.ui_base.components.molecule.card.HalvedCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.IconCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.ImageCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.SmallNotificationMlcData
import ua.gov.diia.ui_base.components.molecule.card.VerticalCardMlcData
import ua.gov.diia.ui_base.components.molecule.card.WhiteCardMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabMoleculeDataV2
import ua.gov.diia.ui_base.components.molecule.header.chiptabbar.ChipTabsOrgData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.media.ArticleVideoMlcData
import ua.gov.diia.ui_base.components.molecule.text.HeadingWithSubtitlesMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelContainerMlcData
import ua.gov.diia.ui_base.components.organism.bottom.BtnIconRoundedGroupOrgData
import ua.gov.diia.ui_base.components.organism.carousel.ArticlePicCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.HalvedCardCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.SimpleCarouselCard
import ua.gov.diia.ui_base.components.organism.carousel.SmallNotificationCarouselOrgData
import ua.gov.diia.ui_base.components.organism.carousel.VerticalCardCarouselOrgData
import ua.gov.diia.ui_base.components.organism.header.MediaTitleOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.atom.text.TickerType as TickerTypeCompose

/**
 *  Atoms
 */

fun BtnPlainIconAtm.toUiModel(
    id: String? = null
): BtnPlainIconAtmData {
    return BtnPlainIconAtmData(
        id = id ?: UIActionKeysCompose.BTN_PLAIN_ICON_ATM,
        label = label.toDynamicString(),
        icon = icon.toDrawableResource(),
        action = action?.toDataActionWrapper(),
        interactionState = when (state) {
            ButtonStates.enabled.name -> UIState.Interaction.Enabled
            ButtonStates.disabled.name -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        }
    )
}

fun ChipStatusAtm.toUiModel(id: String? = null): ChipStatusAtmData {
    return ChipStatusAtmData(
        title = name,
        type = when (type) {
            Type.SUCCESS.id -> StatusChipType.POSITIVE
            Type.PENDING.id -> StatusChipType.PENDING
            Type.FAIL.id -> StatusChipType.NEGATIVE
            Type.NEUTRAL.id -> StatusChipType.NEUTRAL
            else -> {
                StatusChipType.NEUTRAL
            }
        }
    )
}

fun BadgeCounterAtm.toUiModel(): BadgeCounterAtmData {
    return BadgeCounterAtmData(
        count = count
    )
}

fun DoubleIconAtm.toUiModel(id: String? = null): DoubleIconAtmData {
    return DoubleIconAtmData(
        id = id,
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

fun IconAtm.toUiModel(id: String? = null): IconAtmData {
    return IconAtmData(
        id = id,
        componentId = componentId.orEmpty(),
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

fun SmallIconAtm.toUiModel(id: String? = null): SmallIconAtmData {
    return SmallIconAtmData(
        id = id,
        code = code,
        accessibilityDescription = accessibilityDescription,
        action = action?.toDataActionWrapper()
    )
}

fun ArticlePicAtm.toUiModel(id: String? = null): ArticlePicAtmData {
    return ArticlePicAtmData(
        id = id ?: UIActionKeysCompose.ARTICLE_PIC_ATM,
        url = image
    )
}

fun SectionTitleAtm.toUiModel(): SectionTitleAtmData {
    return SectionTitleAtmData(
        label = UiText.DynamicString(label)
    )
}

fun Action.toDataActionWrapper(): DataActionWrapper {
    return DataActionWrapper(
        type = type,
        subtype = subtype,
        resource = resource
    )
}

fun TickerAtm.toUiModel(): TickerAtomData {
    return TickerAtomData(
        componentId = componentId.orEmpty(),
        title = value,
        type = when (type) {
            TickerAtm.TickerType.warning -> TickerTypeCompose.SMALL_WARNING
            TickerAtm.TickerType.positive -> TickerTypeCompose.SMALL_POSITIVE
            TickerAtm.TickerType.neutral -> TickerTypeCompose.SMALL_NEUTRAL
            TickerAtm.TickerType.informative -> TickerTypeCompose.SMALL_INFORMATIVE
        },
        action = action?.toDataActionWrapper()
    )
}

/**
 *  Molecule
 */

fun HeadingWithSubtitlesMlc.toUiModel(): HeadingWithSubtitlesMlcData {
    return HeadingWithSubtitlesMlcData(
        componentId = this.componentId.orEmpty(),
        value = this.value,
        subtitles = this.subtitles
    )
}

fun BtnIconRoundedMlc.toUiModel(id: String? = null): BtnIconRoundedMlcData {
    return BtnIconRoundedMlcData(
        id = id ?: UIActionKeysCompose.ICON_CARD_MLC,
        label = label.toDynamicStringOrNull(),
        icon = icon.toDrawableResource(),
        action = action?.toDataActionWrapper()
    )
}

fun HalvedCardMlc.toUiModel(id: String? = null): HalvedCardMlcData {
    return HalvedCardMlcData(
        id = this.id ?: id ?: UIActionKeysCompose.HALVED_CARD_MLC,
        imageURL = image,
        label = label.toDynamicString(),
        title = title.toDynamicString(),
        action = action?.toDataActionWrapper()
    )
}

fun IconCardMlc.toUiModel(id: String? = null): IconCardMlcData {
    return IconCardMlcData(
        id = id ?: UIActionKeysCompose.ICON_CARD_MLC,
        label = label.toDynamicString(),
        icon = iconLeft.toDrawableResource(),
        action = action?.toDataActionWrapper()
    )
}

fun ImageCardMlc.toUiModel(id: String? = null, contentDescription: UiText? = null): ImageCardMlcData {
    return ImageCardMlcData(
        id = id ?: UIActionKeysCompose.IMAGE_CARD_MLC,
        title = label.toDynamicString(),
        iconEnd = iconRight.toDrawableResourceOrNull(),
        image = image,
        contentDescription = contentDescription,
        action = action?.toDataActionWrapper()
    )
}

fun SmallNotificationMlc.toUiModel(): SmallNotificationMlcData {
    return SmallNotificationMlcData(
        id = this.id,
        text = text.toDynamicString(),
        label = label.toDynamicString(),
        action = action?.toDataActionWrapper()
    )
}

fun VerticalCardMlc.toUiModel(
    id: String? = null,
    contentDescription: UiText? = null
): VerticalCardMlcData {
    return VerticalCardMlcData(
        id = this.id ?: id ?: UIActionKeysCompose.VERTICAL_CARD_MLC,
        url = image,
        contentDescription = contentDescription,
        label = title.toDynamicString() as UiText,
        badge = badgeCounterAtm.toUiModel(),
        action = action?.toDataActionWrapper()
    )
}

fun WhiteCardMlc.toUiModel(id: String? = null): WhiteCardMlcData {
    return WhiteCardMlcData(
        id = id ?: UIActionKeysCompose.ICON_CARD_MLC,
        smallIcon = smallIconAtm?.toUiModel(),
        iconAtm = iconAtm?.toUiModel(),
        doubleIconAtm = doubleIconAtm?.toUiModel(),
        title = title.toDynamicStringOrNull() as UiText,
        label = label.toDynamicString(),
        action = action?.toDataActionWrapper()
    )
}

fun TitleGroupMlc.toUiModel(): TitleGroupMlcData {
    return TitleGroupMlcData(
        leftNavIcon = leftNavIcon?.let {
            TitleGroupMlcData.LeftNavIcon(
                code = it.code,
                accessibilityDescription = it.accessibilityDescription.toDynamicString(),
                action = it.action?.toDataActionWrapper()
            )
        },
        mediumIconRight = mediumIconRight?.let {
            TitleGroupMlcData.MediumIconRight(
                code = it.code,
                action = it.action?.toDataActionWrapper()
            )
        },
        heroText = heroText.toDynamicString(),
        label = label.toDynamicStringOrNull()
    )
}

fun ListItemMlc.toUiModel(id: String? = null): ListItemMlcData {
    return ListItemMlcData(
        id = this.id ?: id ?: UIActionKeysCompose.LIST_ITEM_MLC,
        label = label.toDynamicString(),
        description = description?.toDynamicStringOrNull(),
        iconLeft = iconLeft?.code?.let {
            UiIcon.DrawableResource(it)
        },
        iconRight = iconRight?.code?.let {
            UiIcon.DrawableResource(it)
        },
        logoLeft = logoLeft?.let {
            UiIcon.DynamicIconBase64(it)
        },
        action = action?.toDataActionWrapper(),
        interactionState = when (state) {
            ButtonStates.enabled.name -> UIState.Interaction.Enabled
            ButtonStates.disabled.name -> UIState.Interaction.Disabled
            else -> UIState.Interaction.Enabled
        }
    )
}

fun ArticleVideoMlc.toUiModel(
    id: String? = null
): ArticleVideoMlcData {
    return ArticleVideoMlcData(
        url = source,
    )
}

fun TextLabelContainerMlc.toUiModel(): TextLabelContainerMlcData {
    return TextLabelContainerMlcData(
        data = TextWithParametersData(
            actionKey = UIActionKeysCompose.TEXT_LABEL_CONTAINER_MLC,
            text = text.toDynamicString(),
            parameters = parameters?.let {
                mutableListOf<TextParameter>().apply {
                    it.forEach { apiModel ->
                        add(
                            TextParameter(
                                data = TextParameter.Data(
                                    name = apiModel.data?.name?.toDynamicStringOrNull(),
                                    resource = apiModel.data?.resource?.toDynamicStringOrNull(),
                                    alt = apiModel.data?.alt?.toDynamicStringOrNull(),
                                ),
                                type = apiModel.type
                            )
                        )
                    }
                }
            }
        )
    )
}

/**
 *  Org
 */

fun BtnIconRoundedGroupOrg.toUiModel(id: String? = null): BtnIconRoundedGroupOrgData {
    return BtnIconRoundedGroupOrgData(
        id = id,
        items = mutableListOf<BtnIconRoundedMlcData>().apply {
            items.forEachIndexed { index, item ->
                add(
                    item.btnIconRoundedMlc.toUiModel(id = index.toString())
                )
            }
        }
    )
}

fun ArticlePicCarouselOrg.toUiModel(id: String? = null): ArticlePicCarouselOrgData {
    return ArticlePicCarouselOrgData(
        id = id,
        items = mutableListOf<SimpleCarouselCard>().apply {
            items.forEach {
                it.articlePicAtm?.let {
                    add(it.toUiModel())
                }
                it.articleVideoMlc?.let {
                    add(it.toUiModel())
                }
            }
        } as List<SimpleCarouselCard>
    )
}

fun HalvedCardCarouselOrg.toUiModel(id: String? = null): HalvedCardCarouselOrgData {
    return HalvedCardCarouselOrgData(
        id = id,
        items = mutableListOf<SimpleCarouselCard>().apply {
            items.forEach {
                it.halvedCardMlc?.let {
                    add(it.toUiModel())
                }
                it.iconCardMlc?.let {
                    add(it.toUiModel())
                }
            }
        } as List<SimpleCarouselCard>
    )
}

fun SmallNotificationCarouselOrg.toUiModel(id: String? = null): SmallNotificationCarouselOrgData {
    return SmallNotificationCarouselOrgData(
        id = id,
        items = mutableListOf<SimpleCarouselCard>().apply {
            items.forEach {
                it.smallNotificationMlc?.let {
                    add(it.toUiModel())
                }
                it.iconCardMlc?.let {
                    add(it.toUiModel())
                }
            }
        } as List<SimpleCarouselCard>
    )
}

fun VerticalCardCarouselOrg.toUiModel(id: String? = null): VerticalCardCarouselOrgData {
    return VerticalCardCarouselOrgData(
        id = id,
        items = mutableListOf<VerticalCardMlcData>().apply {
            items.forEach {
                add(it.verticalCardMlc.toUiModel())
            }
        } as List<VerticalCardMlcData>
    )
}

fun ChipTabsOrg.toUiModel(): ChipTabsOrgData {
    return ChipTabsOrgData(
        tabs = SnapshotStateList<ChipTabMoleculeDataV2>().apply {
            items.forEach {
                this.add(
                    ChipTabMoleculeDataV2(
                        id = it.code ?: "",
                        title = it.label,
                        selectionState = if (it.code == preselectedCode) {
                            UIState.Selection.Selected
                        } else {
                            UIState.Selection.Unselected
                        }
                    )
                )
            }
        }
    )
}

fun MediaTitleOrg.toUiModel(): MediaTitleOrgData {
    return MediaTitleOrgData(
        secondaryLabel = secondaryLabel.toDynamicString(),
        title = title.toDynamicString(),
        button = btnPlainIconAtm.toUiModel()
    )
}

fun NavigationPanelMlc.toUiModel(): NavigationPanelMlcData {
    return NavigationPanelMlcData(
        title = label.toDynamicStringOrNull(),
        isContextMenuExist = ellipseMenu?.isNotEmpty() ?: false
    )
}

fun TopGroupOrg.toUiModel(): TopGroupOrgData {
    return TopGroupOrgData(
        navigationPanelMlcData = navigationPanelMlc?.toUiModel(),
        chipTabsOrgData = chipTabsOrg?.toUiModel(),
        titleGroupMlcData = titleGroupMlc?.toUiModel()
    )
}

fun ListItemGroupOrg.toUiModel(id: String? = null): ListItemGroupOrgData {
    return ListItemGroupOrgData(
        id = id,
        itemsList = mutableListOf<ListItemMlcData>().apply {
            items.forEachIndexed { index, item ->
                add(item.toUiModel(index.toString()))
            }
        }
    )
}