package ua.gov.diia.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ua.gov.diia.search.adapters.SearchAdapter
import ua.gov.diia.search.databinding.FragmentSearchBinding
import ua.gov.diia.search.models.SearchResult
import ua.gov.diia.search.models.SearchableItem
import ua.gov.diia.core.util.event.observeUiDataEvent
import ua.gov.diia.core.util.extensions.fragment.hideKeyboard
import ua.gov.diia.core.util.extensions.fragment.setNavigationResult

@AndroidEntryPoint
class SearchF : Fragment() {

    private companion object {
        const val ARGS_KEY = "key"
        const val ARGS_ARRAY_ITEMS = "searchableList"
    }

    private val viewModel: SearchFVM by viewModels()
    private var binding: FragmentSearchBinding? = null
    private var searchAdapter: SearchAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val data = selectDataList() ?: return
        val resultKey = arguments?.getString(ARGS_KEY) ?: return
        viewModel.doInit(data, resultKey)
        arguments?.clear()
    }

    private fun selectDataList(): List<SearchableItem>? {
        return arguments?.getParcelableArray(ARGS_ARRAY_ITEMS)?.mapNotNull { it as? SearchableItem }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = viewLifecycleOwner
                searchAdapter = SearchAdapter { q -> viewModel.findResult(q) }
                rvSearch.adapter = searchAdapter
                etSearchFValue.doAfterTextChanged { q -> viewModel.filter(q.toString()) }
                imgSearchClose.setOnClickListener { findNavController().popBackStack() }
            }
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.displayItemList.observe(viewLifecycleOwner) { items ->
            searchAdapter?.submitList(items) {
                binding?.rvSearch?.scrollToPosition(0)
            }
        }

        viewModel.setResult.observeUiDataEvent(viewLifecycleOwner, this::sendResult)
    }

    private fun sendResult(result: SearchResult) {
        if (result.item == null) return
        setNavigationResult(key = result.key, result = result.item)
        findNavController().popBackStack()
    }


    override fun onPause() {
        super.onPause()
        hideKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onDestroy() {
        searchAdapter = null
        super.onDestroy()
    }
}