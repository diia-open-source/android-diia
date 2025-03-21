package ua.gov.diia.opensource.deeplinkprocessor

import androidx.navigation.NavDirections
import kotlinx.coroutines.flow.MutableStateFlow
import ua.gov.diia.core.di.actions.GlobalActionFocusOnDocument
import ua.gov.diia.core.di.actions.GlobalActionSelectedMenuItem
import ua.gov.diia.core.models.SingleDeeplinkProcessor
import ua.gov.diia.core.models.deeplink.DeepLinkAction
import ua.gov.diia.core.models.deeplink.DeepLinkActionViewDocument
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.home.model.HomeMenuItem
import ua.gov.diia.ui_base.models.homescreen.HomeMenuItemConstructor

class DeepLinkActionViewDocumentProcessor(
    @GlobalActionFocusOnDocument val globalActionFocusOnDocument: MutableStateFlow<UiDataEvent<String>?>,
    @GlobalActionSelectedMenuItem val globalActionSelectedMenuItem: MutableStateFlow<UiDataEvent<HomeMenuItemConstructor>?>,
): SingleDeeplinkProcessor {
    override suspend fun handleDeepLinkAction(linkAction: DeepLinkAction): NavDirections? {
        val action = linkAction as DeepLinkActionViewDocument
        globalActionFocusOnDocument.emit(UiDataEvent(action.documentType))
        globalActionSelectedMenuItem.emit(UiDataEvent(HomeMenuItem.DOCUMENTS))
        return null
    }

    override fun isHandled(action: DeepLinkAction): Boolean = action is DeepLinkActionViewDocument
}