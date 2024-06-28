package ua.gov.diia.documents.ui.fullinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.core.util.extensions.activity.setWindowBrightness
import ua.gov.diia.documents.ui.BottomDoc
import ua.gov.diia.documents.ui.fullinfo.compose.FullInfoBottomSheet
import ua.gov.diia.documents.util.view.showCopyDocIdClipedSnackBar
import ua.gov.diia.ui_base.components.infrastructure.collectAsEffect

@AndroidEntryPoint
class FullInfoFCompose : BottomDoc() {

    private var composeView: ComposeView? = null
    private val args: FullInfoFComposeArgs by navArgs()
    private val viewModel: FullInfoFComposeVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.configureBody(args.doc)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        composeView = ComposeView(requireContext())
        return composeView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        composeView?.setContent {
            val body = viewModel.bodyData
            val progressIndicator =
                viewModel.progressIndicator.collectAsState(initial = Pair("", true))

            viewModel.apply {
                docAction.collectAsEffect { docAction ->
                    when (docAction) {
                        is FullInfoFComposeVM.DocActions.DocNumberCopy -> {
                            composeView?.rootView?.showCopyDocIdClipedSnackBar(
                                docAction.value, 40f
                            )
                        }

                        is FullInfoFComposeVM.DocActions.ItemVerticalValueCopy -> {
                            composeView?.rootView?.showCopyDocIdClipedSnackBar(
                                docAction.value, 40f
                            )
                        }

                        is FullInfoFComposeVM.DocActions.ItemHorizontalValueCopy -> {
                            composeView?.rootView?.showCopyDocIdClipedSnackBar(
                                docAction.value, 40f
                            )
                        }

                        is FullInfoFComposeVM.DocActions.ItemPrimaryValueCopy -> {
                            composeView?.rootView?.showCopyDocIdClipedSnackBar(
                                docAction.value, 40f
                            )
                        }

                        is FullInfoFComposeVM.DocActions.DismissDoc -> {
                            dismiss()
                        }
                        is FullInfoFComposeVM.DocActions.HighBrightness -> {
                            activity?.setWindowBrightness()
                        }

                        is FullInfoFComposeVM.DocActions.DefaultBrightness -> {
                            activity?.setWindowBrightness(true)
                        }
                    }
                }
            }

            FullInfoBottomSheet(progressIndicator = progressIndicator.value, data = body, onUIAction = {
                viewModel.onUIAction(it)
            })
        }
    }

    override fun onPause() {
        super.onPause()
        activity?.setWindowBrightness(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        composeView = null
    }
}