package ua.gov.diia.feed.helper

import ua.gov.diia.ui_base.components.infrastructure.UIElementData

interface FeedOfflineScreenContentProvider {

    fun getOfflineBody(): List<UIElementData>

    fun getOfflineToolbar(firstName: String?): List<UIElementData>

}