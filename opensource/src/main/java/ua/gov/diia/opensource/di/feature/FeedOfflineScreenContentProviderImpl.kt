package ua.gov.diia.opensource.di.feature

import ua.gov.diia.feed.R
import ua.gov.diia.feed.helper.FeedOfflineScreenContentProvider
import ua.gov.diia.ui_base.components.DiiaResourceIcon
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.button.BtnIconRoundedMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.organism.bottom.BtnIconRoundedGroupOrgData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData

class FeedOfflineScreenContentProviderImpl : FeedOfflineScreenContentProvider {
    private companion object {
        const val ACTION_TYPE_QR_SCAN = "qr"
    }

    override fun getOfflineBody(): List<UIElementData> {
        val btnIconRoundedGroupOrg = BtnIconRoundedGroupOrgData(
            items = mutableListOf<BtnIconRoundedMlcData>().apply {
                add(
                    BtnIconRoundedMlcData(
                        icon = UiIcon.DrawableResource(
                            code = DiiaResourceIcon.QR_SCAN_WHITE.code
                        ),
                        id = "",
                        action = DataActionWrapper(
                            type = ACTION_TYPE_QR_SCAN
                        ),
                        label = UiText.StringResource(resId = R.string.qr_scan_label)
                    )
                )
            } as List<BtnIconRoundedMlcData>
        )
        return listOf(btnIconRoundedGroupOrg)
    }

    override fun getOfflineToolbar(firstName: String?): List<UIElementData> {
        val topGroupOrg = TopGroupOrgData(
            titleGroupMlcData = TitleGroupMlcData(
                heroText = firstName?.let {
                    UiText.StringResource(R.string.feed_greeting_offline_with_name, it)
                } ?: UiText.StringResource(R.string.feed_greeting_offline),
                componentId = UiText.StringResource(R.string.feed_test_tag)
            )
        )
        return listOf(topGroupOrg)
    }
}

