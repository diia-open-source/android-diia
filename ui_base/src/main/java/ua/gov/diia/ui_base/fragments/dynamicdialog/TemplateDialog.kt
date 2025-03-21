package ua.gov.diia.ui_base.fragments.dynamicdialog

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.navigate
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.ui_base.R
import ua.gov.diia.ui_base.UiBaseConst
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.infrastructure.screen.TemplateDialogScreen
import kotlin.math.roundToInt

@AndroidEntryPoint
class TemplateDialogF : DialogFragment() {

    private val viewModel: TemplateDialogVM by viewModels()
    private val navArgs: TemplateDialogFArgs by navArgs()
    private var composeView: ComposeView? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.apply {
            setTransparentBackground()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)

                // Blur radius in DP
                attributes = attributes.apply { blurBehindRadius = 16 }
            }
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(navArgs.dialog)
    }

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
        composeView?.setContent {
            viewModel.apply {
                navigation.collectAsEffect { navigation ->
                    when (navigation) {
                        is TemplateDialogVM.Navigation.DismissDialog -> processDismissUiEvent()
                        is TemplateDialogVM.Navigation.OnDialogAction -> processDialogAction(
                            navigation.action
                        )
                    }
                }
            }

            TemplateDialogScreen(
                dataState = viewModel.uiData,
                onUIAction = { viewModel.onUIAction(it) }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        configureStyle()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }

    private fun processDialogAction(action: String) {
        when (val resultKey = resultKey()) {
            ActionsConst.KEY_GLOBAL_PROCESSING -> {
                when (action) {
                    UiBaseConst.DIALOG_ACTION_CODE_PROLONG -> navigateToProlong()
                    else -> setResult(action, resultKey)
                }
            }

            else -> setResult(action, resultKey)
        }
    }

    private fun navigateToProlong() {
        navigate(
            TemplateDialogFDirections.actionTemplateDialogToDestinationProlong()
        )
    }

    private fun processDismissUiEvent() {
        setResult(ActionsConst.DIALOG_ACTION_CODE_CLOSE, resultKey())
    }

    private fun setResult(action: String, key: String) {
        //If the arbitrary ID has been set it should use the new flow with the dialog popUp behaviour
        val destinationId = navArgs.arbitaryDestinationId
        if (destinationId != -1) {
            setNavigationResult(
                arbitraryDestination = destinationId,
                data = ConsumableString(action),
                key = key
            )
            findNavController().popBackStack()
        } else {
            setNavigationResult(result = ConsumableString(action), key = key)
        }
    }

    private fun resultKey() = navArgs.dialog.key ?: ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY

    private fun configureStyle() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar)
    }

    private fun Window.setTransparentBackground() {
        val insetHorizontal = resources.getDimension(R.dimen.xlarge).roundToInt()

        setBackgroundDrawable(
            InsetDrawable(
                ColorDrawable(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.transparent
                    )
                ),
                insetHorizontal,
                0,
                insetHorizontal,
                0
            )
        )
    }
}