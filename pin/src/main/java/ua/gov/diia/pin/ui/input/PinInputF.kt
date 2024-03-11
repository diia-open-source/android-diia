package ua.gov.diia.pin.ui.input

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
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import ua.gov.diia.pin.ui.input.compose.PinInputScreen
import javax.inject.Inject

@AndroidEntryPoint
class PinInputF : Fragment() {

    private val viewModel: PinInputVM by viewModels()
    private val args: PinInputFArgs by navArgs()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var pinHelper: PinHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.verification)
    }

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
                        is PinInputVM.Navigation.PinApproved -> {
                            pinApproved()
                        }

                        is PinInputVM.Navigation.ToQr -> {
                            navigateToQr()
                        }

                        is PinInputVM.Navigation.ToHome -> {
                            navigateToHome()
                        }

                        is PinInputVM.Navigation.AlternativeAuth -> {
                            pinHelper.openAlternativeAuth(this@PinInputF, viewModel)
                        }
                    }
                }
                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }

                PinInputScreen(
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
                ActionsConst.DIALOG_ACTION_CODE_LOGOUT -> viewModel.resetPin()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun pinApproved() {
        findNavController().popBackStack()
    }

    private fun navigateToHome() {
        navigate(
            PinInputFDirections.actionDestinationPinInputToHomeF()
        )
    }

    private fun navigateToQr() {
        navigate(
            PinInputFDirections.actionDestinationPinInputToQrScanF()
        )
    }

}