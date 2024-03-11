package ua.gov.diia.documents.util

import kotlinx.coroutines.flow.MutableSharedFlow
import ua.gov.diia.core.util.event.UiEvent
import ua.gov.diia.ui_base.components.infrastructure.event.DocAction
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData

open class BaseDocActionItemProcessor(val code: String, val name: String) {

    fun generateListItem(itemCode: String, manualActionName: String = ""): ListItemMlcData? {
        if (itemCode == code) {
            return getListItem(manualActionName)
        }
        return null
    }

    protected open fun getListItem(manualActionName: String): ListItemMlcData? {
        return null
    }

    fun processEvent(event: String, docAction: MutableSharedFlow<DocAction>, dismiss: MutableSharedFlow<UiEvent>, navigation: MutableSharedFlow<NavigationPath>) {
        when(event) {
            code -> processCode(docAction, dismiss, navigation)
            name -> processName(docAction, dismiss, navigation)
        }
    }

    protected open fun processCode(docAction: MutableSharedFlow<DocAction>, dismiss: MutableSharedFlow<UiEvent>, navigation: MutableSharedFlow<NavigationPath>) {}
    protected open fun processName(docAction: MutableSharedFlow<DocAction>, dismiss: MutableSharedFlow<UiEvent>, navigation: MutableSharedFlow<NavigationPath>) {}

}