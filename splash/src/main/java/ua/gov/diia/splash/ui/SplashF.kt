package ua.gov.diia.splash.ui

import androidx.compose.runtime.Composable
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.dialogs.TemplateDialogModel
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.currentDestinationId
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.registerForNavigationResultOnce
import ua.gov.diia.core.util.extensions.fragment.registerForTemplateDialogNavResultOnce
import ua.gov.diia.splash.helper.SplashHelper
import ua.gov.diia.splash.ui.compose.SplashScreen
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.fragments.BaseComposeFragment
import ua.gov.diia.ui_base.util.navigation.openTemplateDialog
import javax.inject.Inject

@AndroidEntryPoint
class SplashF : BaseComposeFragment<SplashFVM>() {

    override val viewModel: SplashFVM by viewModels()

    @Inject
    lateinit var splashHelper: SplashHelper

    override fun buildViewModel(viewModel: SplashFVM) {
        val args: SplashFArgs by navArgs()
        viewModel.doInit(
            args.skipInitialization,
            args.uuid4
        )
    }

    @Composable
    override fun Content(viewModel: SplashFVM) {
        viewModel.navigation.collectAsEffect { navigation ->
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
        viewModel.showTemplateDialog.collectAsEffect {
            navigateToTemplateDialog(it.peekContent())
        }

        SplashScreen(
            dataState = viewModel.uiData,
            onEvent = { viewModel.onUIAction(it) }
        )
    }

    private fun navigateToPinCreation() {
        registerForNavigationResultOnce(RESULT_KEY_PIN, viewModel::setServiceUserPin)

        splashHelper.navigateToProtectionCreation(
            host = this,
            resultDestinationId = currentDestinationId ?: return,
            resultKey = RESULT_KEY_PIN,
        )
    }

    private fun navigateToTemplateDialog(model: TemplateDialogModel) {
        registerForTemplateDialogNavResultOnce { action ->
            findNavController().popBackStack()
            when (action) {
                ActionsConst.DIALOG_ACTION_RESUME -> viewModel.resumeSplashJobs()
            }
        }

        openTemplateDialog(model)
    }

    private companion object {
        const val RESULT_KEY_PIN = "SplashF.RESULT_KEY_PIN"
    }
}
