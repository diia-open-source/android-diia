package ua.gov.diia.search.ui.bullet_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ua.gov.diia.search.databinding.FragmentSearchBulletsBinding
import ua.gov.diia.search.models.SearchableBullet
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult

class SearchBulletF : Fragment() {

    private val args: SearchBulletFArgs by navArgs()
    private val viewModel: SearchBulletVM by viewModels { SearchBulletVMF() }
    private var binding: FragmentSearchBulletsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBulletsBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                viewModel = this@SearchBulletF.viewModel
                ivBack.setOnClickListener { findNavController().popBackStack() }
            }

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setArgs(args.data, args.screenHeader, args.contentTitle)
        viewModel.setNavigationResult.observeUiDataEvent(viewLifecycleOwner, ::setResult)
    }

    private fun setResult(enum: SearchableBullet) {
        setNavigationResult(
            key = args.resultKey,
            result = enum
        )
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}