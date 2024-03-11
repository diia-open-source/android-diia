package ua.gov.diia.opensource.ui.fragments.system

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.screen.SystemDialogScreen


class SystemDialog : DialogFragment() {

    private val viewModel: SystemDialogVM by viewModels()
    private val args: SystemDialogArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.dialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        isCancelable = args.dialog.cancelable
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        composeView?.setContent {
            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is SystemDialogVM.Navigation.SendActionResult -> {
                            sendResult(navigation.result)
                        }
                    }
                }
            }

            SystemDialogScreen(
                dataState = viewModel.uiData,
                onEvent = { viewModel.onUIAction(it) }
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun sendResult(action: String) {
        val resultKey = args.resultKey ?: ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
        setNavigationResult(result = ConsumableString(action), key = resultKey)
        findNavController().popBackStack()
    }
}