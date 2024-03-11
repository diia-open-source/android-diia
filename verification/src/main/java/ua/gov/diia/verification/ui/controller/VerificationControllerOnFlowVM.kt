package ua.gov.diia.verification.ui.controller

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.gov.diia.core.util.alert.ClientAlertDialogsFactory
import ua.gov.diia.core.util.delegation.WithErrorHandlingOnFlow
import ua.gov.diia.core.util.delegation.WithRetryLastAction
import ua.gov.diia.core.util.event.UiDataEvent
import ua.gov.diia.core.util.extensions.vm.executeActionOnFlow
import ua.gov.diia.core.util.system.application.ApplicationLauncher
import ua.gov.diia.core.util.system.application.InstalledApplicationInfoProvider
import ua.gov.diia.core.util.system.service.SystemServiceProvider
import ua.gov.diia.verification.model.VerificationFlowResult
import ua.gov.diia.verification.model.VerificationMethodView
import ua.gov.diia.verification.model.VerificationMethodsData
import ua.gov.diia.verification.model.VerificationMethodsView
import ua.gov.diia.verification.model.VerificationResult
import ua.gov.diia.verification.network.ApiVerification
import ua.gov.diia.verification.ui.VerificationSchema
import ua.gov.diia.verification.ui.methods.VerificationMethod
import ua.gov.diia.verification.ui.methods.VerificationNavRequest

abstract class VerificationControllerOnFlowVM(
    protected val apiVerification: ApiVerification,
    private val clientAlertDialogsFactory: ClientAlertDialogsFactory,
    private val applicationInfoProvider: InstalledApplicationInfoProvider,
    private val systemServiceProvider: SystemServiceProvider,
    private val applicationLauncher: ApplicationLauncher,
    private val retryErrorBehavior: WithRetryLastAction,
    private val errorHandlingBehaviour: WithErrorHandlingOnFlow,
    private val verificationMethods: Map<String, @JvmSuppressWildcards VerificationMethod>
) : ViewModel(),
    WithRetryLastAction by retryErrorBehavior,
    WithErrorHandlingOnFlow by errorHandlingBehaviour {


    private var flowProcessId: String? = null
    private var verificationSchema: String? = null
    protected var preferredVerificationMethodCode: String? = null
    private var preferredVerificationMethodUrl: String? = null
    private var preferredVerificationMethodBank: String? = null
    private var preferredVerificationMethodBankRequestId: String? = null
    private var isExternalAppHasBeenRequested = false
    private var verificationRequestData: VerificationMethodsData? = null

    private val _verifyingUser = MutableStateFlow(false)
    protected val verifyingUser = _verifyingUser.asStateFlow()

    private val _navigateToMethodSelectionDialog =
        MutableSharedFlow<UiDataEvent<VerificationMethodsView>>(
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
        )
    val navigateToMethodsSelectionDialog = _navigateToMethodSelectionDialog.asSharedFlow()

    private val _navigateToVerification = MutableSharedFlow<UiDataEvent<VerificationNavRequest>>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val navigateToVerification = _navigateToVerification.asSharedFlow()

    protected var authMethodsSkipped: Boolean = false

    protected abstract fun doOnVerificationCompleted(result: VerificationResult)

    @VisibleForTesting(VisibleForTesting.PROTECTED)
    fun startVerification(schema: String) {
        cleanUpAllData()
        verificationSchema = schema
        getVerificationMethods()
    }

    private fun cleanUpAllData() {
        flowProcessId = null
        verificationSchema = null
        preferredVerificationMethodBank = null
        cleanUpVerificationMethodCache()
    }

    private fun cleanUpVerificationMethodCache() {
        preferredVerificationMethodCode = null
        preferredVerificationMethodUrl = null
        preferredVerificationMethodBankRequestId = null
        verificationRequestData = null
        isExternalAppHasBeenRequested = false
    }

    fun getVerificationMethods() {
        //clean up the previously selected preferred method before start the new verification step
        cleanUpVerificationMethodCache()

        val schema = verificationSchema ?: return

        executeActionOnFlow(
            progressIndicator = _verifyingUser,
            templateKey = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
        ) {
            with(doVerificationMethodsApiCall(schema)) {
                if (template != null) {
                    showTemplateDialog(
                        template,
                        VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
                    )
                } else {
                    processVerificationMethods()
                }
            }
        }
    }

    protected suspend fun doVerificationMethodsApiCall(schema: String): VerificationMethodsData {
        return withContext(Dispatchers.Default) {
            apiVerification.getVerificationMethods(schema, flowProcessId).also {
                flowProcessId = it.processId
                verificationRequestData = it
            }
        }
    }

    fun processVerificationMethods() {
        verificationRequestData?.doOnVerificationMethodsApproved { methods, data ->
            if (methods.size == 1) {
                launchVerificationMethod(methods.first())
            } else {
                launchVerificationMethodsSelection(methods, data)
            }
        }
    }

    protected fun VerificationMethodsData.doOnVerificationMethodsApproved(
        launchVerification: (methods: List<String>, data: VerificationMethodsData) -> Unit
    ) {
        if (methods != null) {
            viewModelScope.launch {
                val availableMethods =
                    methods.filter { x -> verificationMethods[x]?.isAvailable == true }

                //Shows the template dialog if there are no methods after filtering
                if (availableMethods.isEmpty()) {
                    val template = methods.singleOrNull()?.let {
                        verificationMethods[it]?.getUnavailabilityDialog()
                    } ?: clientAlertDialogsFactory.getNoVerificationMethodsDialog()

                    showTemplateDialog(
                        template,
                        VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
                    )
                } else {
                    launchVerification.invoke(
                        availableMethods,
                        this@doOnVerificationMethodsApproved
                    )
                }
            }
        } else {
            if (skipAuthMethods == true) {
                authMethodsSkipped = true
                this@VerificationControllerOnFlowVM.flowProcessId = processId
                completeVerification()
            } else {
                val template = clientAlertDialogsFactory.getNoVerificationMethodsDialog()
                showTemplateDialog(
                    template,
                    VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
                )
            }
        }
    }

    private fun launchVerificationMethodsSelection(
        availableMethods: List<String>,
        data: VerificationMethodsData
    ) {
        viewModelScope.launch {
            val verificationMethods = availableMethods.mapNotNull { it.toVerificationMethod() }

            val methodSelectionRequest = VerificationMethodsView(
                title = data.title,
                buttonAction = data.actionButton?.action ?: return@launch,
                methods = verificationMethods,
                schema = verificationSchema ?: return@launch
            )
            _navigateToMethodSelectionDialog.tryEmit(UiDataEvent(methodSelectionRequest))
        }
    }

    private fun String.toVerificationMethod() = verificationMethods[this]?.let {
        VerificationMethodView(
            code = this,
            iconRes = it.iconResId,
        )
    }

    private fun launchVerificationMethod(code: String) {
        preferredVerificationMethodCode = code

        val verificationMethod = verificationMethods[code] ?: return
        executeActionOnFlow(
            progressIndicator = _verifyingUser,
            templateKey = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
        ) {
            val result = verificationMethod.getVerificationRequest(
                verificationSchema = verificationSchema ?: return@executeActionOnFlow,
                processId = flowProcessId ?: return@executeActionOnFlow
            )
            if (result.url != null) {
                if (result.url.template != null) {
                    showTemplateDialog(
                        result.url.template,
                        VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
                    )
                } else {
                    preferredVerificationMethodUrl = result.url.authUrl
                }
            }
            if (result.shouldLaunchUrl) {
                result.url?.authUrl?.let { url ->
                    applicationLauncher.launch(url)
                    isExternalAppHasBeenRequested = true
                }
            } else if (result.navRequest != null) {
                _navigateToVerification.emit(UiDataEvent(result.navRequest))
            }
        }
    }

    fun cleanUpAndLaunchVerificationMethod(schema: String, result: VerificationFlowResult) {
        cleanUpAllData()
        verificationSchema = schema
        cleanUpVerificationMethodCache()

        executeActionOnFlow(
            progressIndicator = _verifyingUser,
            templateKey = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
        ) {
            doVerificationMethodsApiCall(schema)
            handleVerificationResult(result)
        }
    }

    fun handleVerificationResult(result: VerificationFlowResult) {
        when (result) {

            is VerificationFlowResult.VerificationMethod -> launchVerificationMethod(result.method)

            is VerificationFlowResult.CompleteVerificationStep -> {
                preferredVerificationMethodBankRequestId = result.requestId
                preferredVerificationMethodBank = result.bankCode
                completeVerificationStep(result)
            }
        }
    }

    fun completeBankAppVerificationStep() {
        if (isExternalAppHasBeenRequested) {
            //clean up the bank verification step request after it has been handled
            //because it will be triggered every time when the VerificationControllerF state
            //will be changed to ON_RESUME
            isExternalAppHasBeenRequested = false
            completeVerificationStep(null)
        }
    }

    private fun completeVerificationStep(result: VerificationFlowResult.CompleteVerificationStep?) {
        val processId = flowProcessId ?: return
        val method = preferredVerificationMethodCode ?: return
        val requestId = result?.requestId ?: getRequestIdFromAuthUrl() ?: return

        executeActionOnFlow(
            progressIndicator = _verifyingUser,
            templateKey = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
        ) {
            val template = apiVerification.completeVerificationStep(
                method,
                requestId,
                processId,
                result?.bankCode ?: preferredVerificationMethodBank
            ).template

            showTemplateDialog(
                templateDialog = template,
                key = VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
            )
        }
    }

    fun completeVerification() {
        val result = when (verificationSchema) {

            VerificationSchema.GENERATE_SIGNATURE -> VerificationResult.GenerateSignature(
                resourceId = getRequestIdFromAuthUrl() ?: return,
                flowId = flowProcessId ?: return
            )

            else -> VerificationResult.Common(flowProcessId!!)
        }

        doOnVerificationCompleted(result)
    }

    fun completedVerifyResidentPermit() {
        doOnVerificationCompleted(VerificationResult.Common(flowProcessId!!))
    }

    protected fun getRequestIdFromAuthUrl(): String? {
        return preferredVerificationMethodUrl
            ?.substringAfterLast('/')
            ?.takeUnless { it.isEmpty() }
    }
}