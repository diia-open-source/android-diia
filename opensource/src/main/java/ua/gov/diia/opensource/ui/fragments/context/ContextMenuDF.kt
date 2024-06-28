package ua.gov.diia.opensource.ui.fragments.context

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.documents.R
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect
import ua.gov.diia.ui_base.components.organism.ContextMenuOrg
import ua.gov.diia.ui_base.fragments.BaseBottomDialog

class ContextMenuDF : BaseBottomDialog() {

    private val viewModel: ContextMenuDVM by viewModels()
    private val args: ContextMenuDFArgs by navArgs()

    private var composeView: ComposeView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.init(args.items.toList())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
            window?.setWindowAnimations(R.style.BottomDialogAnimation)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {

            viewModel.apply {
                navigation.collectAsEffect {
                    when (it) {
                        ContextMenuDVM.ContextMenuNavigation.CloseMenu -> {
                            dismiss()
                        }

                        is ContextMenuDVM.ContextMenuNavigation.NavigateByAction -> {
                            dismiss()
                            setNavigationResult(
                                result = ConsumableString(it.action.getActionType()),
                                key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
                            )
                        }
                    }

                }

            }
            ContextMenuOrg(
                modifier = Modifier,
                contextMenu = args.items.toList(),
                onUIAction = {
                    viewModel.onUIAction(it)
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}