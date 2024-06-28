package ua.gov.diia.login.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.delegation.WithAppConfig
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.login.ui.compose.LoginScreen
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.verification.ui.controller.VerificationControllerOnFlowF
import ua.gov.diia.web.util.extensions.fragment.navigateToWebView
import javax.inject.Inject

@AndroidEntryPoint
class LoginF : VerificationControllerOnFlowF() {

    override val verificationVM: LoginVM by viewModels()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var appConfig: WithAppConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerForNavigationResultOnce(RESULT_KEY_CREATE_PIN, verificationVM::setPinCode)

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> verificationVM.retryLastAction()
            }
        }
        composeView?.setContent {
            verificationVM.apply {
                val isLoading = verificationVM.isLoading.collectAsState(
                    initial = Pair(
                        UIActionKeysCompose.PAGE_LOADING_TRIDENT,
                        true
                    )
                )
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is LoginVM.Navigation.ToPolicy -> {
                            navigateToWebView(appConfig.getAppPolicyUrl())
                        }

                        is LoginVM.Navigation.ToPinCreation -> {
                            navigateToPinCreation()
                        }

                        is LoginVM.Navigation.ToHome -> {
                            navigateToHomeScreen()
                        }
                    }
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }

                LoginScreen(
                    toolbar = verificationVM.toolbarData,
                    body = verificationVM.bodyData,
                    bottom = verificationVM.bottomData,
                    screenNavigationProcessing = verificationVM.progressState,
                    isLoading = isLoading.value,
                    onEvent = { verificationVM.onUIAction(it) }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToPinCreation() {
        navigate(
            LoginFDirections.actionLoginToCreatePin(
                resultDestinationId = currentDestinationId ?: return,
                resultKey = RESULT_KEY_CREATE_PIN,
                flowType = CreatePinFlowType.AUTHORIZATION
            )
        )
    }

    private fun navigateToHomeScreen() {
        navigate(
            LoginFDirections.actionDestinationLoginToHomeF(
                launchFlow = ConsumableString(ActionsConst.ACTION_AUTH_FLOW)
            )
        )
    }

    private companion object {
        const val RESULT_KEY_CREATE_PIN = "create_pin"
    }
}