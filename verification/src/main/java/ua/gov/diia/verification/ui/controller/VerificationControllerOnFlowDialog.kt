package ua.gov.diia.verification.ui.controller


import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForDialogNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ui_base.fragments.BaseSheetDialog
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.verification.R
import ua.gov.diia.verification.model.VerificationMethodsView
import ua.gov.diia.verification.ui.controller.VerificationControllerConst.VERIFICATION_ALERT_DIALOG_ACTION
import ua.gov.diia.verification.ui.method_selection.VerificationMethodSelectionDFArgs
import ua.gov.diia.verification.ui.methods.VerificationNavRequest

abstract class VerificationControllerOnFlowDialog : BaseSheetDialog() {

    private companion object {
        const val RESULT_KEY_VERIFICATION_STEP =
            "VerificationControllerF.RESULT_KEY_VERIFICATION_STEP"

        const val ACTION_LOAD_VERIFICATION_METHODS = "getMethods"
        const val ACTION_LAUNCH_VERIFICATION = "showMethods"

        //complete flow actions
        const val ACTION_LOGIN_COMPLETE = "getToken"
        const val ACTION_SIGNATURE_GENERATION_COMPLETE = "pinCreation"
        const val ACTION_INPUT_PIN = "inputPin"
        const val ACTION_PROLONG = "prolong"
    }

    private val featureCompleteList = listOf(
        ACTION_SIGNATURE_GENERATION_COMPLETE,
        ACTION_LOGIN_COMPLETE,
        ACTION_INPUT_PIN,
        ACTION_PROLONG
    )

    abstract val verificationVM: VerificationControllerOnFlowVM

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(verificationVM) {
                    launch {
                        showTemplateDialog.collect {
                            openTemplateDialog(it.peekContent())
                        }
                    }
                    launch {
                        navigateToMethodsSelectionDialog.collect {
                            navigateToMethodSelection(it.peekContent())
                        }
                    }
                    launch {
                        navigateToVerification.collect {
                            navigateToVerification(it.peekContent())
                        }
                    }
                }
            }
        }

        registerForTemplateDialogNavResult(VERIFICATION_ALERT_DIALOG_ACTION) { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> verificationVM.retryLastAction()
                ActionsConst.DIALOG_DEAL_WITH_IT -> verificationVM.completedVerifyResidentPermit()
                ACTION_LOAD_VERIFICATION_METHODS -> verificationVM.getVerificationMethods()
                ACTION_LAUNCH_VERIFICATION -> verificationVM.processVerificationMethods()
                in featureCompleteList -> verificationVM.completeVerification()
            }
        }

        registerForDialogNavigationResultOnce(
            RESULT_KEY_VERIFICATION_STEP,
            verificationVM::handleVerificationResult
        )
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        verificationVM.completeBankAppVerificationStep()
    }

    private fun navigateToMethodSelection(data: VerificationMethodsView) {
        val args = VerificationMethodSelectionDFArgs(
            resultDestinationId = currentDestinationId ?: return,
            resultKey = RESULT_KEY_VERIFICATION_STEP,
            data = data
        )
        findNavController().navigate(
            R.id.action_global_destination_verificationMethodSelection,
            args.toBundle()
        )
    }

    private fun navigateToVerification(request: VerificationNavRequest) {
        val direction = request.getNavDirection(currentDestinationId ?: return, RESULT_KEY_VERIFICATION_STEP)
        navigate(direction)
    }
}