package ua.gov.diia.publicservice.ui.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ua.gov.diia.publicservice.models.toDisplayService
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceView
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.util.navigation.generateComposeNavigationPanel
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import javax.inject.Inject

@HiltViewModel
class PublicServicesSearchComposeVM @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction,
    private val composeMapper: PublicServicesSearchComposeMapper,
    private val helper: PublicServiceHelper
) : ViewModel(), WithErrorHandlingOnFlow by errorHandling,
    WithRetryLastAction by retryLastAction,
    PublicServicesSearchComposeMapper by composeMapper,
    PublicServiceHelper by helper {

    private val _toolBarData = mutableStateListOf<UIElementData>()
    val toolBarData: SnapshotStateList<UIElementData> = _toolBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private companion object {
        const val MIN_QUERY_SIZE = 3
    }

    private var categories: List<PublicServiceCategory> =
        emptyList()
    private var displayList: List<PublicServiceView> =
        emptyList()

    val query = MutableLiveData<String?>()
    private val queryFlow = query.asFlow()


    private val _services =
        MutableSharedFlow<List<PublicServiceView>>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val services = _services.asLiveData()

    init {
        viewModelScope.launch {
            queryFlow.collect(::filterServices)
        }
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                event.action?.type?.let {
                    val serviceView = event.data?.let { findPublicService(it) }
                    if (serviceView != null) {
                        val service =
                            categories.find { category -> category.code == serviceView.categoryCode }?.publicServices?.find { service -> service.code == serviceView.code }
                                ?: return
                        _navigation.tryEmit(
                            PublicServicesCategoriesSearchNavigation.NavigateToService(
                                service
                            )
                        )
                    }
                }
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                val newValue = event.data
                query.value = newValue
            }
        }
    }

    private fun findPublicService(code: String): PublicServiceView? {
        return services.value?.find {
            it.categoryCode
            it.code == code
        }
    }

    private fun onSearch(query: String?) {
        val index = _bodyData.indexOfFirst {
            it is SearchInputV2Data
        }
        if (index == -1) {
            return
        } else {
            _bodyData[index] =
                (_bodyData[index] as SearchInputV2Data).onChange(query)
        }

    }

    fun doInit(data: Array<PublicServiceCategory>) {
        categories = data.toList()
        prepareDisplayList(data)
        _toolBarData.addIfNotNull(
            generateComposeNavigationPanel(title = "Сервіси")
        )
        _bodyData.addIfNotNull(
            generateSearchInputMoleculeV2("Що шукаєте?", 0)
        )
    }

    private fun prepareDisplayList(data: Array<PublicServiceCategory>) {
        executeActionOnFlow(dispatcher = dispatcher.work) {
            displayList = data.flatMap { category ->
                category.publicServices.map { service ->
                    service.toDisplayService(category.name, category.code)
                }
            }
        }
    }

    private fun filterServices(query: String?) {
        if (query != null && query.length >= MIN_QUERY_SIZE) {

            executeActionOnFlow(dispatcher = dispatcher.work) {
                val displayServices = displayList.filter { service ->
                    service.search.contains(query, ignoreCase = true)
                }
                _services.tryEmit(displayServices)
            }
        } else {
            _services.tryEmit(emptyList())

        }
        onSearch(query)
        services.observeForever {

            val index = _bodyData.indexOfFirst {
                it is ListItemGroupOrgData
            }
            if (index == -1) {
                _bodyData.addIfNotNull(
                    it.toComposeListItemGroupOrgData()
                )
            } else {
                _bodyData[index] = it.toComposeListItemGroupOrgData()
            }
            configureEmptyBody(it.isEmpty(), query)

        }


    }

    private fun configureEmptyBody(isServicesExist: Boolean, query: String?) {
        val data = StubMessageMlcData(
            icon = UiText.DynamicString("\uD83E\uDD37\u200D♂️"),
            title = UiText.DynamicString("Не знайдено жодної послуги")
        )
        val index = _bodyData.indexOfFirst {
            it is StubMessageMlcData
        }
        if (isServicesExist && query != null && query.length > MIN_QUERY_SIZE) {
            if (index == -1) {
                _bodyData.addIfNotNull(
                    data.toComposeEmptyStateErrorMoleculeData()
                )
            } else {
                _bodyData[index] = data.toComposeEmptyStateErrorMoleculeData()
            }
        } else {
            if (index != -1) {
                _bodyData.removeAt(index)
            }
        }
    }

}

sealed class PublicServicesCategoriesSearchNavigation : NavigationPath {

    data class NavigateToService(val service: PublicService) :
        PublicServicesCategoriesSearchNavigation()
}