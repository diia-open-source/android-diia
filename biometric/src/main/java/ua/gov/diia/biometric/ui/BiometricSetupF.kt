package ua.gov.diia.biometric.ui

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
import ua.gov.diia.biometric.ui.compose.BiometricSetupScreen
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import javax.inject.Inject

@AndroidEntryPoint
class BiometricSetupF : Fragment() {

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    private val viewModel: BiometricSetupVM by viewModels()
    private val args: BiometricSetupFArgs by navArgs()
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
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BiometricSetupVM.Navigation.CompletePinCreation -> {
                            completeFlow()
                        }
                    }
                }
            }

            BiometricSetupScreen(
                data = uiDataElements,
                onUIAction = { viewModel.onUIAction(it) },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun completeFlow() {
        setNavigationResult(
            arbitraryDestination = args.resultDestinationId,
            key = args.resultKey,
            data = args.pin
        )
        findNavController().popBackStack(args.resultDestinationId, false)
    }

}