package ua.gov.diia.ui_base.components.infrastructure.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrg
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

@Composable
fun TopBarRootContainer(
    modifier: Modifier = Modifier,
    topBarViews: SnapshotStateList<UIElementData>,
    diiaResourceIconProvider: DiiaResourceIconProvider,
    onUIAction: (UIAction) -> Unit
) {
    topBarViews.forEach {
        when (it) {
            is TopGroupOrgData -> {
                TopGroupOrg(
                    modifier = modifier,
                    data = it,
                    onUIAction = onUIAction,
                    diiaResourceIconProvider = diiaResourceIconProvider,
                )
            }
        }
    }
}

@Preview
@Composable
fun TopBarRootContainerPreview() {
    val _topBarData = remember { mutableStateListOf<UIElementData>() }
    val toolbarData: SnapshotStateList<UIElementData> = _topBarData
    _topBarData.add(
        TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = UiText.StringResource(resId = R.string.notification_channel_messages),
                leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                    code = CommonDiiaResourceIcon.BACK.code,
                    accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                    action = DataActionWrapper(
                        type = "back",
                        subtype = null,
                        resource = null
                    )
                )
            )
        )
    )
    TopBarRootContainer(
        topBarViews = toolbarData,
        diiaResourceIconProvider = DiiaResourceIconProvider.forPreview(),
    ) {

    }
}