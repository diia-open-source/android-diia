package ua.gov.diia.verification.ui.method_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.ui_base.fragments.BaseBottomDialog
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.verification.databinding.DialogVerificationMethodsBinding
import ua.gov.diia.verification.model.VerificationFlowResult

@AndroidEntryPoint
internal class VerificationMethodSelectionDF : BaseBottomDialog() {

    private val viewModel: VerificationMethodSelectionVM by viewModels()
    private val args: VerificationMethodSelectionDFArgs by navArgs()
    private var binding: DialogVerificationMethodsBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.doInit(args.data)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        binding = DialogVerificationMethodsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                vm = viewModel

                btnCloseMethodsSelection.setOnClickListener { findNavController().popBackStack() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.sendResult.observeUiDataEvent(viewLifecycleOwner, ::sendResult)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun sendResult(code: String) {
        setNavigationResult(
            arbitraryDestination = args.resultDestinationId,
            key = args.resultKey,
            data = VerificationFlowResult.VerificationMethod(code)
        )
        findNavController().popBackStack()
    }
}