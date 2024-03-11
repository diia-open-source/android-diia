package ua.gov.diia.login.ui

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import ua.gov.diia.core.di.actions.GlobalActionLazy
import ua.gov.diia.core.di.data_source.http.UnauthorizedClient
import ua.gov.diia.core.models.ActionDataLazy
import ua.gov.diia.core.network.apis.ApiAuth
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_DEBUG
import ua.gov.diia.core.util.CommonConst.BUILD_TYPE_STAGE
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithBuildConfig
import ua.gov.diia.core.util.delegation.WithCrashlytics
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.lifecycle.consumeEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.service.SystemServiceProvider
import ua.gov.diia.diia_storage.store.repository.authorization.AuthorizationRepository
import ua.gov.diia.login.R
import ua.gov.diia.login.network.ApiLogin
import ua.gov.diia.pin.repository.LoginPinRepository
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmData
import ua.gov.diia.ui_base.components.atom.space.SpacerAtmType
import ua.gov.diia.ui_base.components.atom.text.textwithparameter.TextParameter
import ua.gov.diia.ui_base.components.infrastructure.UIElementData
import ua.gov.diia.ui_base.components.infrastructure.addAllIfNotNull
import ua.gov.diia.ui_base.components.infrastructure.event.UIAction
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.components.infrastructure.findAndChangeFirstByInstance
import ua.gov.diia.ui_base.components.infrastructure.navigation.NavigationPath
import ua.gov.diia.ui_base.components.infrastructure.state.UIState
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiIcon
import ua.gov.diia.ui_base.components.infrastructure.utils.resource.UiText
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxBorderedMlcData
import ua.gov.diia.ui_base.components.molecule.checkbox.CheckboxSquareMlcData
import ua.gov.diia.ui_base.components.molecule.header.TitleGroupMlcData
import ua.gov.diia.ui_base.components.molecule.list.ListItemMlcData
import ua.gov.diia.ui_base.components.molecule.text.TextLabelMlcData
import ua.gov.diia.ui_base.components.organism.header.TopGroupOrgData
import ua.gov.diia.ui_base.components.organism.list.ListItemGroupOrgData
import ua.gov.diia.verification.model.VerificationFlowResult
import ua.gov.diia.verification.model.VerificationMethodsData
import ua.gov.diia.verification.model.VerificationResult
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.ui.VerificationSchema
import ua.gov.diia.verification.ui.controller.VerificationControllerConst
import ua.gov.diia.verification.ui.controller.VerificationControllerOnFlowVM
import ua.gov.diia.verification.ui.methods.VerificationMethod
import javax.inject.Inject

@HiltViewModel
class LoginVM @Inject constructor(
    @GlobalActionLazy private val actionLazy: MutableSharedFlow<UiDataEvent<ActionDataLazy>>,
    @UnauthorizedClient private val apiAuth: ApiAuth,
    @UnauthorizedClient apiVerification: ApiVerification,
    @UnauthorizedClient private val apiLogin: ApiLogin,
    private val loginPinRepository: LoginPinRepository,
    private val authorizationRepository: AuthorizationRepository,
    private val postLoginActions: Set<@JvmSuppressWildcards PostLoginAction>,
    clientAlertDialogsFactory: ClientAlertDialogsFactory,
    retryErrorBehavior: WithRetryLastAction,
    errorHandlingBehaviour: WithErrorHandlingOnFlow,
    applicationInfoProvider: InstalledApplicationInfoProvider,
    systemServiceProvider: SystemServiceProvider,
    applicationLauncher: ApplicationLauncher,
    private val verificationMethods: Map<String, @JvmSuppressWildcards VerificationMethod>,
    private val withCrashlytics: WithCrashlytics,
    withBuildConfig: WithBuildConfig,
) : VerificationControllerOnFlowVM(
    apiVerification,
    clientAlertDialogsFactory,
    applicationInfoProvider,
    systemServiceProvider,
    applicationLauncher,
    retryErrorBehavior,
    errorHandlingBehaviour,
    verificationMethods
), WithBuildConfig by withBuildConfig {

    private var authToken: String? = null

    private val _navigation = MutableSharedFlow<NavigationPath>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigation = _navigation.asSharedFlow()

    private val _loadingIndicatorKey = MutableStateFlow(UIActionKeysCompose.PAGE_LOADING_TRIDENT)
    private val _dataLoading = MutableStateFlow(value = false)
    val isLoading = combine(_dataLoading, verifyingUser) { dataLoadingState, verifyingUserState ->
        if (verifyingUserState) {
            _loadingIndicatorKey.emit(UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING)
        }
        dataLoadingState || verifyingUserState
    }.combine(_loadingIndicatorKey) { value, key ->
        key to value
    }
    private val _screenProgressState = mutableStateOf(value = false)
    val progressState: State<Boolean> = _screenProgressState

    private val _toolbarData = mutableStateListOf<UIElementData>()
    val toolbarData: SnapshotStateList<UIElementData> = _toolbarData

    private val _bodyData = mutableStateListOf<UIElementData>()
    val bodyData: SnapshotStateList<UIElementData> = _bodyData

    init {
        if (getBuildType() == BUILD_TYPE_STAGE || getBuildType() == BUILD_TYPE_DEBUG) {

            fun loadToken(data: ActionDataLazy) {
                viewModelScope.launch {
                    try {
                        authToken = apiAuth.getTestToken(data.hard, data.hardMap).token
                        _navigation.tryEmit(Navigation.ToPinCreation)
                    } catch (e: Exception) {
                        withCrashlytics.sendNonFatalError(e)
                    }
                }
            }

            viewModelScope.launch {
                actionLazy.collectLatest { dataEvent ->
                    dataEvent.consumeEvent(::loadToken)
                }
            }
        }

        loadAuthMethodData()
    }

    private fun displayStaticUI() {
        _toolbarData.add(
            TopGroupOrgData(
                titleGroupMlcData = TitleGroupMlcData(
                    heroText = UiText.StringResource(R.string.login_screen_title_text),
                    label = UiText.StringResource(
                        R.string.login_screen_title_subtext,
                        getVersionName(),
                    )
                )
            )
        )

        _bodyData.addAllIfNotNull(
            TextLabelMlcData(
                actionKey = LoginConst.ACTION_NAVIGATE_TO_POLICY,
                text = UiText.StringResource(R.string.login_screen_description_text),
                parameters = listOf(
                    TextParameter(
                        type = "link",
                        data = TextParameter.Data(
                            name = UiText.StringResource(R.string.login_screen_description_param_type),
                            alt = UiText.StringResource(R.string.login_screen_description_param_text),
                            resource = UiText.StringResource(R.string.login_screen_description_param_link),
                        )
                    )
                )
            ),
            CheckboxBorderedMlcData(
                actionKey = LoginConst.ACTION_CHECKBOX_CHECKED,
                data = CheckboxSquareMlcData(
                    actionKey = LoginConst.ACTION_CHECKBOX_CHECKED,
                    title = UiText.StringResource(R.string.login_screen_checkbox_text),
                    interactionState = UIState.Interaction.Enabled,
                    selectionState = UIState.Selection.Selected,
                    contentDescription = UiText.StringResource(R.string.accessibility_login_screen_checkbox_text)
                )
            )
        )
    }

    private fun loadAuthMethodData() {
        executeActionOnFlow(
            progressIndicator = _dataLoading.also {
                _loadingIndicatorKey.tryEmit(UIActionKeysCompose.PAGE_LOADING_TRIDENT)
            },
            templateKey = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
        ) {
            val data = doVerificationMethodsApiCall(VerificationSchema.AUTHORIZATION)
            processVerificationRequestData(data)
        }
    }

    private fun processVerificationRequestData(verificationRequestData: VerificationMethodsData) {
        verificationRequestData.doOnVerificationMethodsApproved { methods, _ ->
            displayStaticUI()
            val state =
                if (isCheckBoxSelected()) UIState.Interaction.Enabled else UIState.Interaction.Disabled
            val verificationMethods = methods.mapNotNull { m ->
                verificationMethods[m]?.toListItemAtomData(state)
            }
            val list = SnapshotStateList<ListItemMlcData>()
            list.addAll(verificationMethods)
            _bodyData.add(
                ListItemGroupOrgData(
                    title = UiText.StringResource(R.string.login_screen_bank_list_title),
                    itemsList = list
                )
            )
            _bodyData.add(SpacerAtmData(SpacerAtmType.SPACER_32))
        }
    }

    private fun VerificationMethod.toListItemAtomData(
        state: UIState.Interaction
    ): ListItemMlcData? = if (isAvailableForAuth) {
        ListItemMlcData(
            id = name,
            label = UiText.StringResource(titleResId),
            logoLeft = UiIcon.DrawableResInt(iconResId),
            interactionState = state,
            logoLeftContentDescription = UiText.StringResource(descriptionResId),
        )
    } else {
        null
    }

    override fun doOnVerificationCompleted(result: VerificationResult) {
        if (result is VerificationResult.Common) {
            getToken(result.processId)
        }
    }

    private fun getToken(processId: String) {
        executeActionOnFlow(progressIndicator = _dataLoading) {
            authToken = apiLogin.getAuthenticationToken(processId).token
            _navigation.tryEmit(Navigation.ToPinCreation)
        }
    }

    fun setPinCode(pin: String) {
        executeActionOnFlow {
            authorizationRepository.setToken(authToken!!)
            loginPinRepository.setUserAuthorized(pin)
            postLoginActions.forEach { action ->
                launch {
                    runCatching {
                        action.onPostLogin()
                    }.onFailure(withCrashlytics::sendNonFatalError)
                }
            }
            _navigation.tryEmit(Navigation.ToHome)
        }
    }

    fun onUIAction(uiAction: UIAction) {
        when (uiAction.actionKey) {
            LoginConst.ACTION_NAVIGATE_TO_POLICY -> {
                _navigation.tryEmit(Navigation.ToPolicy)
            }

            UIActionKeysCompose.LIST_ITEM_GROUP_ORG -> {
                val code = uiAction.data ?: return
                cleanUpAndLaunchVerificationMethod(
                    VerificationSchema.AUTHORIZATION,
                    VerificationFlowResult.VerificationMethod(code)
                )
            }

            LoginConst.ACTION_CHECKBOX_CHECKED -> {

                _bodyData.findAndChangeFirstByInstance<CheckboxBorderedMlcData> {
                    val result = it.onOptionsCheckChanged()
                    val isSelected = result.data.selectionState == UIState.Selection.Selected
                    _bodyData.findAndChangeFirstByInstance<ListItemGroupOrgData> { listV2 ->
                        val updatedList = SnapshotStateList<ListItemMlcData>()
                        listV2.itemsList.forEach { e ->
                            updatedList.add(e.copy(interactionState = if (isSelected) UIState.Interaction.Enabled else UIState.Interaction.Disabled))
                        }
                        listV2.copy(itemsList = updatedList)
                    }
                    result
                }
            }
        }
    }

    @VisibleForTesting
    fun isCheckBoxSelected() = bodyData.findLast { it is CheckboxBorderedMlcData }
        ?.let { (it as? CheckboxBorderedMlcData)?.data?.selectionState == UIState.Selection.Selected }
        ?: false

    sealed class Navigation : NavigationPath {
        object ToPolicy : Navigation()
        object ToPinCreation : Navigation()
        object ToHome : Navigation()
    }
}