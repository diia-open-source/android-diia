package ua.gov.diia.splash.ui

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
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResult
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.splash.ui.compose.SplashScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import javax.inject.Inject

@AndroidEntryPoint
class SplashF : Fragment() {

    private val viewModel: SplashFVM by viewModels()
    private val args: SplashFArgs by navArgs()
    private var composeView: ComposeView? = null

    @Inject
    lateinit var splashHelper: SplashHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(
            args.skipInitialization,
            args.uuid4
        )
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
            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is SplashFVM.Navigation.ToErrorDialog -> {
                            navigate(SplashFDirections.actionSplashFToRootDF(navigation.diiaError))
                        }
                        is SplashFVM.Navigation.ToPinCreation -> {
                            navigateToPinCreation()
                        }
                        is SplashFVM.Navigation.ToLogin -> {
                            navigate(SplashFDirections.actionSplashFToDestinationLogin())
                        }
                        is SplashFVM.Navigation.ToQrScanner -> {
                            navigate(SplashFDirections.actionSplashFToQrScanF())
                        }
                        is SplashFVM.Navigation.ToProtection -> {
                            navigate(SplashFDirections.actionSplashFToDestinationPinInput())
                        }
                    }
                }

                showTemplateDialog.collectAsEffect {
                    openTemplateDialog(it.peekContent())
                }
            }

            SplashScreen(
                dataState = viewModel.uiData,
                onEvent = { viewModel.onUIAction(it) }
            )
        }

        registerForNavigationResultOnce(RESULT_KEY_PIN, viewModel::setServiceUserPin)

        registerForTemplateDialogNavResult { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.DIALOG_ACTION_RESUME -> viewModel.resumeSplashJobs()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun navigateToPinCreation() {
        splashHelper.navigateToProtectionCreation(
            host = this,
            resultDestinationId = currentDestinationId ?: return,
            resultKey = RESULT_KEY_PIN,
        )
    }

    private companion object {
        const val RESULT_KEY_PIN = "SplashF.RESULT_KEY_PIN"
    }
}
