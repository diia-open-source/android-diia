package ua.gov.diia.ui_base.fragments.errordialog

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
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateDialogScreen

class ErrorDF : DialogFragment() {

    private val shareVm: ErrorDVM by activityViewModels()
    private var composeView: ComposeView? = null
    private val args: ErrorDFArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        isCancelable = false
        return composeView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        shareVm.constructDialog(args.error)
        composeView?.setContent {
            TemplateDialogScreen(
                dataState = shareVm.uiData,
                onUIAction = { shareVm.onUIAction(it) }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()
        setStyle(STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar)
    }
}