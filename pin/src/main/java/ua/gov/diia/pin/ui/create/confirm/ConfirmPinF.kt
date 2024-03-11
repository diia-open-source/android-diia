package ua.gov.diia.pin.ui.create.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.pin.helper.PinHelper
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.pin.ui.create.compose.CreatePinScreen
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import javax.inject.Inject

@AndroidEntryPoint
class ConfirmPinF : Fragment() {

    private val viewModel: ConfirmPinVM by viewModels()
    private val args: ConfirmPinFArgs by navArgs()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var pinHelper: PinHelper

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.flowType, args.pin)
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
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is ConfirmPinVM.Navigation.ToAlternativeAuthSetup -> {
                            navigateToBiometric(navigation.pin)
                        }

                        is ConfirmPinVM.Navigation.PinCreation -> {
                            completeFlow(navigation.pin)
                        }

                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }
                    }
                }
                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
            }
            CreatePinScreen(
                data = uiDataElements,
                onUIAction = { viewModel.onUIAction(it) },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }
        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.DIALOG_DEAL_WITH_IT -> {
                    viewModel.matchedPin.value?.let { completeFlow(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun completeFlow(pin: String) {
        setNavigationResult(
            arbitraryDestination = args.resultDestinationId,
            key = args.resultKey,
            data = pin
        )
        findNavController().popBackStack(args.resultDestinationId, false)
    }

    private fun navigateToBiometric(pin: String) {
        pinHelper.navigateToAlternativeAuthSetup(
            host = this,
            resultDestinationId = args.resultDestinationId,
            resultKey = args.resultKey,
            pin = pin,
        )
    }
}