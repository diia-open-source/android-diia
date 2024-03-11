package ua.gov.diia.core.util.delegation

import androidx.lifecycle.LiveData
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.event.UiDataEvent

interface WithContextMenu<T : ContextMenuField> {

    val openContextMenu: LiveData<UiDataEvent<Array<T>>>

    val showContextMenu: LiveData<Boolean>

    val faqNavDirection: NavDirections?

    fun openContextMenu()

    fun setContextMenu(contextMenu: Array<T>?)

    fun getMenu(): Array<T>?
}