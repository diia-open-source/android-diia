package ua.gov.diia.ps_criminal_cert.util

import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavDirections
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.util.delegation.WithContextMenu
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData

class StubContextMenu<T : ContextMenuField> : WithContextMenu<T> {

    private var contextMenu: Array<T>? = null

    private val _openContextMenu = MutableLiveData<UiDataEvent<Array<T>>>()
    override val openContextMenu = _openContextMenu.asLiveData()

    private val _showContextMenu = MutableLiveData(false)
    override val showContextMenu = _showContextMenu.asLiveData()

    override val faqNavDirection: NavDirections? = null

    override fun openContextMenu() {
        contextMenu?.let {
            _openContextMenu.postValue(UiDataEvent(it))
        }
    }

    override fun setContextMenu(contextMenu: Array<T>?) {
        this.contextMenu = contextMenu

        val isVisible = !contextMenu.isNullOrEmpty()
        _showContextMenu.postValue(isVisible)
    }

    override fun getMenu(): Array<T>?  = contextMenu

}