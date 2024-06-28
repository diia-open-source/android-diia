package ua.gov.diia.publicservice.ui.categories.compose

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.data.repository.DataRepository
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.DispatcherProvider
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.lifecycle.asLiveData
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.publicservice.R
import ua.gov.diia.publicservice.di.DataRepositoryPublicServiceCategories
import ua.gov.diia.publicservice.helper.PublicServiceHelper
import ua.gov.diia.publicservice.models.PublicService
import ua.gov.diia.publicservice.models.PublicServiceCategory
import ua.gov.diia.publicservice.models.PublicServiceTab
import ua.gov.diia.publicservice.models.PublicServicesCategories
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.addIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import javax.inject.Inject

@HiltViewModel
class PublicServicesCategoriesComposeVM @Inject constructor(
    @DataRepositoryPublicServiceCategories private val repository: DataRepository<PublicServicesCategories?>,
    private val helper: PublicServiceHelper,
    private val dispatcherProvider: DispatcherProvider,
    private val retryLastAction: WithRetryLastAction,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val composeMapper: PublicServicesCategoriesTabMapper,
) : ViewModel(),
    WithErrorHandlingOnFlow by errorHandling,
    PublicServicesCategoriesTabMapper by composeMapper,
    WithRetryLastAction by retryLastAction,
    PublicServiceHelper by helper {

    private val _topBarData = mutableStateListOf<UIElementData>()
    val topBarData: SnapshotStateList<UIElementData> = _topBarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    private var categoryToOpen: String? = null

    private var selectedTab: String? = null
    private var categoriesData =
        PublicServicesCategories(
            listOf(),
            listOf()
        )

    private val _tabs = MutableLiveData<List<PublicServiceTab>>(emptyList())
    val tabs = _tabs.asLiveData()

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _publicServices = MutableLiveData<List<PublicServiceCategory>>()
    val publicServices = _publicServices.asLiveData()

    private val _contentLoadedKey = MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_TRIDENT)
    private val _contentLoaded = MutableStateFlow(false)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to (value || bodyData.isNotEmpty())
        }

    fun doInit(categoryToOpen: String?) {
        this.categoryToOpen = categoryToOpen
        getCategoriesData()
    }

    fun onUIAction(event: UIAction) {
        when (event.actionKey) {
            UIActionKeysCompose.CHIP_TABS_MOLECULE -> {
                event.data?.let { onTabSelected(it) }
            }

            UIActionKeysCompose.PS_ITEM_CLICK -> {
                val cat = event.data?.let { findCategory(it) }
                if (cat != null) {
                    doOnCategorySelected(cat)
                }
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                navigateCategoriesServicesSearch()
            }
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }
        }
    }

    private fun configureTopBar() {
        if (_topBarData.isEmpty()) {
            val toolbar = TopGroupOrgData(
                titleGroupMlcData = TitleGroupMlcData(
                    heroText = UiText.DynamicString("Сервіси"),
                    componentId = UiText.StringResource(R.string.home_title_services_test_tag)
                )
            )
            _topBarData.addIfNotNull(toolbar)
        }
    }

    private fun getCategoriesData() {
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT
        }) {
            repository.load().let { data ->
                if (data != null && data.categories.isNotEmpty()) {
                    if (categoriesData != data) {
                        categoriesData = data
                        refreshContentList()
                        forceToOpenCategory(data.categories)
                    }
                }
                configureTopBar()
            }
        }
    }

    private fun forceToOpenCategory(categories: List<PublicServiceCategory>) {
        viewModelScope.launch(dispatcherProvider.work) {
            categoryToOpen?.let { categoryCode ->
                categoryToOpen = null
                categories.find { category -> category.code == categoryCode }
                    ?.run(::doOnCategorySelected)
            }
        }
    }

    private fun onTabSelected(type: String) {
        selectedTab = type
        refreshContentList()
    }


    private fun refreshContentList() {
        val availableTabs = categoriesData.tabs
        if (selectedTab == null) {
            selectedTab = availableTabs.firstOrNull()?.code
        }
        val filteredCategories = if (availableTabs.size > 1) {
            categoriesData.categories.filter { ct ->
                ct.tabCode == selectedTab
            }
        } else {
            categoriesData.categories
        }
        _tabs.value = availableTabs.map {
            val selected = it.code == selectedTab
            if (selected == it.isChecked) {
                it
            } else {
                it.copy(isChecked = selected)
            }
        }
        _publicServices.value = filteredCategories

        _bodyData.clear()
        _bodyData.addAllIfNotNull(
            generateSearchInputMoleculeV2("Пошук", 1)
        )
        _bodyData.addAllIfNotNull(generateComposeChipTabBarV2(tabs.value, selectedTab))
        _bodyData.addAllIfNotNull(filteredCategories.toComposeServiceTileOrganism())
    }

    private fun navigateCategoriesServicesSearch() {
        _navigation.tryEmit(
            PublicServicesCategoriesNavigation.NavigateToServiceSearch(
                categoriesData.categories.toTypedArray()
            )
        )
    }

    private fun doOnCategorySelected(category: PublicServiceCategory) {
        if (category.hasServices && category.status.enabled) {
            if (category.isSingleServiceCategory) {
                val service = category.singleService ?: return
                if (service.status.enabled) {
                    _navigation.tryEmit(
                        PublicServicesCategoriesNavigation.NavigateToService(
                            service
                        )
                    )
                } else {
                    return
                }
            } else {
                _navigation.tryEmit(
                    PublicServicesCategoriesNavigation.NavigateToCategory(
                        category
                    )
                )
            }
        }
    }

    private fun findCategory(code: String): PublicServiceCategory? {
        return _publicServices.value?.find {
            it.code == code
        }
    }
}

sealed class PublicServicesCategoriesNavigation : NavigationPath {
    data class NavigateToCategory(val category: PublicServiceCategory) :
        PublicServicesCategoriesNavigation()

    data class NavigateToService(val service: PublicService) : PublicServicesCategoriesNavigation()

    data class NavigateToServiceSearch(val data: Array<PublicServiceCategory>) :
        PublicServicesCategoriesNavigation()
}