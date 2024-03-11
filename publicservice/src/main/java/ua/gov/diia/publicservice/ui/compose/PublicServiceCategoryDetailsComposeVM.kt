package ua.gov.diia.publicservice.ui.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.ui_base.util.navigation.generateComposeNavigationPanel
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import javax.inject.Inject

@HiltViewModel
class PublicServiceCategoryDetailsComposeVM @Inject constructor(
    private val helper: PublicServiceHelper,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val composeMapper: PublicServiceCategoryDetailsComposeMapper
) : ViewModel(), WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by retryLastAction,
    PublicServiceCategoryDetailsComposeMapper by composeMapper,
    PublicServiceHelper by helper {

    private val _toolBarData = mutableStateListOf<UIElementData>()
    val toolBarData: SnapshotStateList<UIElementData> = _toolBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _category = MutableLiveData<PublicServiceCategory>()
    val category = _category.asLiveData()

    fun doInit(category: PublicServiceCategory) {
        _category.value = category
        _toolBarData.addIfNotNull(
            generateComposeNavigationPanel(title = category.name)
        )
        _bodyData.addIfNotNull(
            category.publicServices.toComposeListItemGroupOrg()
        )
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                event.action?.type?.let {
                    val service = event.data?.let { findPublicService(it) }
                    if (service != null) {
                        _navigation.tryEmit(
                            PublicServicesCategoriesDetailsNavigation.NavigateToService(
                                service
                            )
                        )
                    }
                }
            }
        }
    }

    private fun findPublicService(code: String): PublicService? {
        val snapshot = _category.value ?: return null
        return snapshot.publicServices.find {
            it.code == code
        }
    }
}

sealed class PublicServicesCategoriesDetailsNavigation : NavigationPath {

    data class NavigateToService(val service: PublicService) :
        PublicServicesCategoriesDetailsNavigation()
}