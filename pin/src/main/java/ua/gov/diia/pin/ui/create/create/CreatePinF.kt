package ua.gov.diia.pin.ui.create.create

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
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.pin.ui.create.compose.CreatePinScreen
import ua.gov.diia.ui_base.navigation.BaseNavigation

@AndroidEntryPoint
class CreatePinF : Fragment() {

    private val viewModel: CreatePinVM by viewModels()
    private val args: CreatePinFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.flowType)
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
                        is CreatePinVM.Navigation.ToPinConformation -> {
                            navigateToConformation(navigation.pin)
                        }

                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }
                    }
                }
            }

            CreatePinScreen(
                data = uiDataElements,
                onUIAction = { viewModel.onUIAction(it) }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToConformation(pin: String) {
        navigate(
            CreatePinFDirections.actionDestinationCreatePinToDestinationConfirmPin(
                args.resultDestinationId, args.resultKey, args.flowType, pin
            )
        )
    }
}