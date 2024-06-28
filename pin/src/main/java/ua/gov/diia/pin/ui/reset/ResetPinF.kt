package ua.gov.diia.pin.ui.reset

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.pin.model.CreatePinFlowType
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.pin.ui.reset.compose.ResetPinScreen

@AndroidEntryPoint
class ResetPinF : Fragment() {

    private val viewModel: ResetPinVM by viewModels()
    private val args: ResetPinFArgs by navArgs()
    private var composeView: ComposeView? = null

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
        composeView?.setContent {
            val uiDataElements = viewModel.uiData
            viewModel.apply {
                val validationFinished =
                    validationFinished.collectAsState(initial = UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_UI_BLOCKING to true)
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is ResetPinVM.Navigation.CreateNewPin -> {
                            navigateToCreation()
                        }

                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }
                    }
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
                ResetPinScreen(
                    data = uiDataElements,
                    contentLoaded = validationFinished.value,
                    onUIAction = { viewModel.onUIAction(it) }
                )
            }
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.DIALOG_DEAL_WITH_IT -> findNavController().popBackStack()
                ActionsConst.DIALOG_ACTION_CODE_LOGOUT -> viewModel.resetPin()
            }
        }
    }

    private fun navigateToCreation() {
        navigate(
            ResetPinFDirections.actionPinResetToCreatePin(
                resultDestinationId = args.resultDestination,
                resultKey = args.resultKey,
                flowType = CreatePinFlowType.RESET_PIN
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}