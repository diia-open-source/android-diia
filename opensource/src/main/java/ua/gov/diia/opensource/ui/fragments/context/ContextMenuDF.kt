package ua.gov.diia.opensource.ui.fragments.context

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.gov.diia.core.models.ConsumableString
import ua.gov.diia.core.models.ContextMenuField
import ua.gov.diia.core.ui.dynamicdialog.ActionsConst
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult
import ua.gov.diia.opensource.databinding.DialogContextMenuBinding
import ua.gov.diia.ui_base.fragments.BaseBottomDialog

class ContextMenuDF : BaseBottomDialog() {

    private val args: ContextMenuDFArgs by navArgs()
    private var binding: DialogContextMenuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogContextMenuBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner

            recyclerView.adapter = ContextMenuListAdapter(args.items.toList()) {
                setResult(it)
            }
            btnClose.setOnClickListener { findNavController().popBackStack() }
        }
        return binding?.root
    }

    private fun setResult(item: ContextMenuField) {
        dismiss()
        setNavigationResult(
            result = ConsumableString(item.getActionType()),
            key = ActionsConst.FRAGMENT_USER_ACTION_RESULT_KEY
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}