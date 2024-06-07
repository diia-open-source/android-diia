package ua.gov.diia.documents.ui.stack.order

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
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.screen.StackOrderScreen

@AndroidEntryPoint
class StackOrderFCompose : Fragment() {

    private var composeView: ComposeView? = null
    private val viewModel: StackOrderVMCompose by viewModels()
    private val args: StackOrderFComposeArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.doc)
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
            val topBar = viewModel.topBarData
            val body = viewModel.bodyData

            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BaseNavigation.Back -> {
                            findNavController().popBackStack()
                        }

                        is StackOrderVMCompose.Navigation.ToStackTypedOrder -> {
                            navigate(
                                StackOrderFComposeDirections.actionGlobalToStackOrder(
                                    navigation.type
                                )
                            )
                        }
                    }
                }
                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
            }
            StackOrderScreen(
                toolbar = topBar,
                body = body,
                onUIAction = { viewModel.onUIAction(it) },
                onMove = { a, b -> viewModel.onMove(a, b) }
            )
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveCurrentOrder()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}