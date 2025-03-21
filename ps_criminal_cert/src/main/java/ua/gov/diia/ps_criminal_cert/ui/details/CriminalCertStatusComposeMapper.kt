package ua.gov.diia.ps_criminal_cert.ui.details

import androidx.compose.runtime.snapshots.SnapshotStateList
import ua.gov.diia.core.models.common.NavigationPanel
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertDetails.LoadAction
import ua.gov.diia.ui_base.components.atom.button.BtnLoadPlainIconAtmData
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextWithParametersData
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.toDynamicString
import ua.gov.diia.ui_base.components.molecule.button.BtnLoadIconPlainGroupMlcData
import ua.gov.diia.ui_base.components.molecule.header.NavigationPanelMlcData
import ua.gov.diia.ui_base.components.molecule.message.StatusMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TitleLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import javax.inject.Inject

interface CriminalCertStatusComposeMapper {

    fun NavigationPanel?.toTopGroupOrg(): TopGroupOrgData?
    fun String?.toComposeTitleLabelMlc(): TitleLabelMlcData?
    fun CriminalCertDetails.StatusMessage?.toStatusMessageMlcOldApi(): StatusMessageMlcData?
    fun List<LoadAction>?.toBtnIconGroupMlc(): BtnLoadIconPlainGroupMlcData?
}

class CriminalCertStatusComposeMapperImpl @Inject constructor() : CriminalCertStatusComposeMapper {

    override fun NavigationPanel?.toTopGroupOrg(): TopGroupOrgData? {
        val navPanel: NavigationPanel = this ?: return null
        return TopGroupOrgData(
            navigationPanelMlcData = NavigationPanelMlcData(
                title = navPanel.header?.let {
                    UiText.DynamicString(it)
                }, isContextMenuExist = navPanel.contextMenu?.isNotEmpty() ?: false
            )
        )
    }

    override fun String?.toComposeTitleLabelMlc(): TitleLabelMlcData? {
        if (this == null) return null
        return TitleLabelMlcData(
            label = this
        )
    }

    override fun CriminalCertDetails.StatusMessage?.toStatusMessageMlcOldApi(): StatusMessageMlcData? {
        val statusMessage = this ?: return null
        return StatusMessageMlcData(
            icon = statusMessage.icon,
            title = statusMessage.title ?: "",
            description = TextWithParametersData(
                actionKey = UIActionKeysCompose.TEXT_WITH_PARAMETERS,
                text = statusMessage.text.toDynamicString(),
            )
        )
    }

    override fun List<LoadAction>?.toBtnIconGroupMlc(): BtnLoadIconPlainGroupMlcData? {
        if (this == null) return null
        val data = this
        return BtnLoadIconPlainGroupMlcData(
            componentId = UiText.DynamicString("btn_load_icon_plain_group"),
            items = SnapshotStateList<BtnLoadPlainIconAtmData>().apply {
                data.forEach {
                    add(
                        BtnLoadPlainIconAtmData(
                            id = it.type,
                            icon = UiIcon.DynamicIconBase64(
                                it.icon ?: ""
                            ),
                            label = UiText.DynamicString(it.name),
                            componentId = UiText.DynamicString(it.type),
                            action = DataActionWrapper(type = it.type)
                        )
                    )

                }
            }
        )
    }
}