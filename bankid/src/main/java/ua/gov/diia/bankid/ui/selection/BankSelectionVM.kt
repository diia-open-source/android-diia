package ua.gov.diia.bankid.ui.selection

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import ua.gov.diia.bankid.R
import ua.gov.diia.bankid.model.AuthBank
import ua.gov.diia.bankid.model.BankAuthRequest
import ua.gov.diia.bankid.model.BankSelectionRequest
import ua.gov.diia.bankid.network.ApiBankId
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.ui_base.components.CommonDiiaResourceIcon
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.infrastructure.DataActionWrapper
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.input.SearchInputV2Data
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.message.StubMessageMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.PlainListWithSearchOrganismData
import ua.gov.diia.verification.di.ProviderVerifiedClient
import ua.gov.diia.verification.network.ApiVerification
import javax.inject.Inject

@HiltViewModel
class BankSelectionVM @Inject constructor(
    @ProviderVerifiedClient private val apiBankId: ApiBankId,
    @ProviderVerifiedClient private val apiVerification: ApiVerification,
    private val errorHandling: WithErrorHandlingOnFlow,
    private val retryLastAction: WithRetryLastAction
) : ViewModel(),
    WithRetryLastAction by retryLastAction,
    WithErrorHandlingOnFlow by errorHandling {

    private var selectedBankCode: String? = null
    private var bankSelectionRequest: BankSelectionRequest? = null

    private val _progressIndicator = MutableStateFlow(false)
    val progressIndicator get() = _progressIndicator.asStateFlow()

    private val _contentLoadedKey =
        MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION)
    private val _contentLoaded = MutableStateFlow(true)
    val contentLoaded: Flow<Pair<String, Boolean>> =
        _contentLoaded.combine(_contentLoadedKey) { value, key ->
            key to value
        }

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _toolbarData = mutableStateListOf<UIElementData>()
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData


    fun doInit(data: BankSelectionRequest) {
        bankSelectionRequest = data
    }

    fun loadBanks() {
        _toolbarData.clear()
        _bodyData.clear()
        executeActionOnFlow(contentLoadedIndicator = _contentLoaded.also {
            _contentLoadedKey.value = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION
        }) {
            val response = apiBankId.getBanksList()
            val list = response.banks.mapToPlainItemListMolecule()
            val data = PlainListWithSearchOrganismData(
                searchData = SearchInputV2Data(
                    placeholder = UiText.StringResource(R.string.bank_selection_search_text),
                    contentDescription = UiText.StringResource(R.string.accessibility_bank_selection_search)
                ),
                fullList = list,
                displayedList = list,
                emptyListData = StubMessageMlcData(
                    icon = UiText.StringResource(R.string.bank_selection_empty_icon),
                    title = UiText.StringResource(R.string.bank_selection_empty_title),
                    description = UiText.StringResource(R.string.bank_selection_empty_description)
                )
            )
            displayStaticPagePart()
            _bodyData.add(data)
            _bodyData.add(SpacerAtmData(SpacerAtmType.SPACER_32))
        }
    }

    private fun displayStaticPagePart() {
        _toolbarData.add(
            TopGroupOrgData(
                titleGroupMlcData = TitleGroupMlcData(
                    heroText = UiText.StringResource(R.string.bank_selection_title_text),
                    leftNavIcon = TitleGroupMlcData.LeftNavIcon(
                        code = CommonDiiaResourceIcon.BACK.code,
                        accessibilityDescription = UiText.StringResource(R.string.accessibility_back_button),
                        action = DataActionWrapper(
                            type = UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK,
                            subtype = null,
                            resource = null
                        )
                    )
                )
            )
        )
        _bodyData.add(
            TextLabelMlcData(text = UiText.StringResource(R.string.bank_selection_description_text))
        )
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                _navigation.tryEmit(BaseNavigation.Back)
            }

            UIActionKeysCompose.TITLE_GROUP_MLC -> {
                uiAction.action?.let {
                    when (it.type) {
                        UIActionKeysCompose.TOOLBAR_NAVIGATION_BACK -> {
                            _navigation.tryEmit(BaseNavigation.Back)
                        }

                        else -> {}
                    }
                }
            }

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                uiAction.data?.let { setBankCode(it) }
            }

            UIActionKeysCompose.SEARCH_INPUT -> {
                val searchQ = uiAction.data ?: return
                _bodyData.findAndChangeFirstByInstance<PlainListWithSearchOrganismData> {
                    it.onSearch(searchQ)
                }
            }
        }
    }

    private fun setBankCode(code: String) {
        selectedBankCode = code
        getAuthUrl(code)
    }

    private fun getAuthUrl(bankCode: String) {
        val request = bankSelectionRequest ?: return
        executeActionOnFlow(progressIndicator = _progressIndicator) {
            apiVerification.getAuthUrl(
                request.verificationMethodCode,
                request.processId,
                bankCode
            ).apply {
                template?.let { showTemplateDialog(it) }
                authUrl?.let {
                    _navigation.tryEmit(Navigation.ToBankAuth(BankAuthRequest(it, bankCode)))
                }
            }
        }
    }

    private fun List<AuthBank>.mapToPlainItemListMolecule(): ListItemGroupOrgData {
        val result = mutableStateListOf<ListItemMlcData>()
        forEach { result.add(it.toAtomData()) }
        return ListItemGroupOrgData(itemsList = result)
    }

    private fun AuthBank.toAtomData(): ListItemMlcData {
        val bankName = this.name ?: ""
        return ListItemMlcData(
            id = id,
            label = UiText.DynamicString(bankName),
            iconRight = UiIcon.DrawableResource(CommonDiiaResourceIcon.OUT_LINK.code),
            iconRightContentDescription = UiText.StringResource(
                R.string.accessibility_bank_selection_item_name,
                bankName
            ),
            interactionState = UIState.Interaction.Enabled,
        )
    }

    sealed class Navigation : NavigationPath {
        data class ToBankAuth(val data: BankAuthRequest) : Navigation()
    }
}