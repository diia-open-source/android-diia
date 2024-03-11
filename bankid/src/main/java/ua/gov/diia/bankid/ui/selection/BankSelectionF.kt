package ua.gov.diia.bankid.ui.selection

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
import ua.gov.diia.ui_base.navigation.BaseNavigation
import ua.gov.diia.bankid.model.BankAuthRequest
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.doOnSystemBackPressed
import ua.gov.diia.core.util.extensions.fragment.hideKeyboard
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.ui_base.components.DiiaResourceIconProvider
import ua.gov.diia.ui_base.components.infrastructure.ServiceScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.event.UIActionKeysCompose
import javax.inject.Inject

@AndroidEntryPoint
class BankSelectionF : Fragment() {

    @Inject
    lateinit var diiaResourceIconProvider: DiiaResourceIconProvider

    private val args: BankSelectionFArgs by navArgs()
    private val viewModel: BankSelectionVM by viewModels()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.request)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        doOnSystemBackPressed { navigateBack() }
        viewModel.loadBanks()
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView?.setContent {
            val toolbar = viewModel.toolbarData
            val body = viewModel.bodyData
            val contentLoaded = viewModel.contentLoaded.collectAsState(
                initial = Pair(
                    UIActionKeysCompose.PAGE_LOADING_TRIDENT_WITH_BACK_NAVIGATION,
                    true
                )
            )

            with(viewModel) {
                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is BankSelectionVM.Navigation.ToBankAuth -> {
                            navigateToBankAuth(navigation.data)
                        }

                        is BaseNavigation.Back -> navigateBack()
                    }
                }
            }

            ServiceScreen(
                toolbar = toolbar,
                body = body,
                contentLoaded = contentLoaded.value,
                onEvent = { viewModel.onUIAction(it) },
                diiaResourceIconProvider = diiaResourceIconProvider,
            )
        }

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.GENERAL_RETRY -> viewModel.retryLastAction()
                ActionsConst.DIALOG_ACTION_CANCEL -> navigateBack()
            }
        }
    }

    private fun navigateBack() {
        hideKeyboard()
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToBankAuth(request: BankAuthRequest) {
        navigate(
            BankSelectionFDirections.actionDestinationBankSelectionToDestinationBankAuth(
                resultDestination = args.resultDestination,
                resultKey = args.resultKey,
                requestData = request
            )
        )
    }

}