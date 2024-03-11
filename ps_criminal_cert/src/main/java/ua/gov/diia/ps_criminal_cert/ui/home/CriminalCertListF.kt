package ua.gov.diia.ps_criminal_cert.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState.Error
import androidx.paging.LoadState.Loading
import androidx.paging.LoadState.NotLoading
import androidx.recyclerview.widget.ConcatAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ua.gov.diia.ui_base.adapters.common.PagingLoadStateAdapter
import ua.gov.diia.ps_criminal_cert.databinding.FragmentCriminalCertListBinding
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus.DONE
import ua.gov.diia.ps_criminal_cert.models.enums.CriminalCertStatus.PROCESSING
import ua.gov.diia.ps_criminal_cert.models.response.CriminalCertListData.CertItem
import ua.gov.diia.core.util.extensions.fragment.navigate

@AndroidEntryPoint
class CriminalCertListF : Fragment() {

    private val viewModel: CriminalCertHomeVM by viewModels({ requireParentFragment() })
    private val args: CriminalCertListFArgs by navArgs()
    private var binding: FragmentCriminalCertListBinding? = null

    private var adapter: CriminalCertListAdapter? = null
    private var concatAdapter: ConcatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = CriminalCertListAdapter(::navigateToDetails)
        concatAdapter = adapter?.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter { adapter?.retry() },
            footer = PagingLoadStateAdapter { adapter?.retry() }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCriminalCertListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            vm = viewModel
            status = args.certStatus

            recyclerView.adapter = concatAdapter
            adapter?.refresh()
        }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            adapter?.loadStateFlow?.collectLatest { states ->
                when (val refresh = states.refresh) {
                    is Error -> {
                        when (args.certStatus) {
                            PROCESSING -> viewModel.setProcessingListLoading(false)
                            DONE -> viewModel.setDoneListLoading(false)
                        }
                        viewModel.consumeException(exception = refresh.error as Exception, needRetry = false)
                    }
                    is Loading -> {
                        when (args.certStatus) {
                            PROCESSING -> viewModel.setProcessingListLoading(adapter?.itemCount == 0)
                            DONE -> viewModel.setDoneListLoading(adapter?.itemCount != 0)
                        }
                    }
                    is NotLoading -> {
                        when (args.certStatus) {
                            PROCESSING -> viewModel.setProcessingListLoading(false)
                            DONE -> viewModel.setDoneListLoading(false)
                        }
                    }
                }
            }
        }

        viewModel.listContent(args.certStatus).observe(viewLifecycleOwner) {
            adapter?.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.recyclerView?.adapter = null
        binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter = null
        concatAdapter = null
    }

    private fun navigateToDetails(cert: CertItem) {
        requireParentFragment().navigate(
            CriminalCertHomeFDirections.actionCriminalCertHomeFToCriminalCertDetailsF(
                contextMenu = args.contextMenu,
                certId = cert.id
            )
        )
    }
}