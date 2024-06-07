package ua.gov.diia.opensource.ui.fragments.context

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.mutableSharedFlowOf
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import javax.inject.Inject


@HiltViewModel
class ContextMenuDVM @Inject constructor() : ViewModel() {

    private val _navigation = mutableSharedFlowOf<NavigationPath>()
    val navigation = _navigation.asSharedFlow()

    private var options: List<ContextMenuField>? = null

    fun init(contextMenu: List<ContextMenuField>) {
        options = contextMenu
    }

    fun onUIAction(event: UIAction) {
        when (event.action?.type ?: event.actionKey) {
            ActionsConst.CONTEXT_MENU_CLOSE -> {
                _navigation.tryEmit(ContextMenuNavigation.CloseMenu)
            }

            else -> {
                val actionType = event.action?.type
                val subType = event.action?.subtype
                val contextMenuField =
                    if (actionType != null && subType == null) {
                        options?.find { it.getActionType() == actionType }
                    } else if (actionType != null) {
                        options?.find {
                            it.getActionType() == (event.action as DataActionWrapper).type && it.getSubType() == (event.action as DataActionWrapper).subtype
                        }
                    } else {
                        null
                    }
                contextMenuField?.let {
                    _navigation.tryEmit(ContextMenuNavigation.NavigateByAction(action = it))
                }

            }
        }
    }

    sealed class ContextMenuNavigation : NavigationPath {
        object CloseMenu : ContextMenuNavigation()
        data class NavigateByAction(val action: ContextMenuField) : ContextMenuNavigation()
    }
}