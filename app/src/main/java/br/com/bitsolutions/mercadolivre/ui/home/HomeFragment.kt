package br.com.bitsolutions.mercadolivre.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bitsolutions.mercadolivre.MainActivity.Companion.QUERY
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.FragmentHomeBinding
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResult
import br.com.bitsolutions.mercadolivre.ui.detail.DetailActivity
import br.com.bitsolutions.pagedlist.adapter.PagedListAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    val viewModel by viewModel<HomeViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    protected val disposables = CompositeDisposable()
    var queryText = ""
    private var offset = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding?.root!!

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.resultItems.collectLatest {
                        onLoading(it.isLoading())
                        when (it) {
                            is State.Data -> onSuccessFetchItems(it.data)
                            is State.Error -> onError(it)
                            else -> Unit
                        }
                    }
                }
                launch {
                    viewModel.hasNextPage.collect { searchAdapter.loadEnable = it }
                }
            }
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queryText = activity?.intent?.getStringExtra(QUERY) ?: ""

        initAdapter()
        if (savedInstanceState != null) {
            binding?.pagedListRecyclerView?.getRecyclerView()?.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable("listState"))
        }

        viewModel.resultItems.value.takeIf { it.isData() }?.let {
            offset = (it as? State.Data)?.data?.offset ?: 0
            queryText = (it as? State.Data)?.data?.query ?: ""
            searchAdapter.loadEnable = viewModel.hasNextPage.value

            val itemsSize = (it as? State.Data)?.data?.items?.size ?: 0
            if (itemsSize <= 0) {
                binding?.pagedListRecyclerView?.showFeedbackStatus(
                    imageResource = R.drawable.ic_search_black_24dp,
                    feedbackTitle = R.string.paged_list_search_title,
                    feedbackMessage = R.string.paged_list_search_description,
                )
            }
        } ?: run {
            searchResultItems(queryText)
        }
    }

    fun onLoading(loading: Boolean?) {
        if (loading == true) {
            binding?.loadingView?.visibility = View.VISIBLE
        } else {
            binding?.loadingView?.visibility = View.GONE
        }
    }

    private fun onSuccessFetchItems(data: SearchResult?) {
        binding?.pagedListRecyclerView?.isRefreshing = false
        data?.offset?.let { offset = it }

        if (data?.items?.isEmpty() == true) {
            binding?.pagedListRecyclerView?.showFeedbackStatus(
                imageResource = R.drawable.ic_search_black_24dp,
                feedbackTitle = R.string.paged_list_search_not_found_title,
                feedbackMessage = R.string.paged_list_search_not_found_description,
            )
        } else {
            searchAdapter.submitList(data?.items)
        }
    }

    private fun onError(error: State.Error?) {
        if (error == null) return

        binding?.pagedListRecyclerView?.isRefreshing = false
        if (searchAdapter.itemCount <= 0) {
            binding?.pagedListRecyclerView?.showFeedbackStatus(
                feedbackTitle = R.string.paged_list_generic_error,
                feedbackMessage = R.string.paged_list_verify_connection_label,
                action = {
                    binding?.pagedListRecyclerView.takeUnless { it?.isRefreshing == true }?.isRefreshing = true
                    viewModel.getSearchResult("MLB", queryText, offset)
                },
            )
        } else {
            searchAdapter.showLoadMoreError()
        }
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter()
        binding?.pagedListRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.pagedListRecyclerView?.getRecyclerView()?.setHasFixedSize(true)
        binding?.pagedListRecyclerView?.adapter = this.searchAdapter

        searchAdapter.loadMoreListener = object : PagedListAdapter.ILoadMoreListener {
            override fun onLoadMore() {
                viewModel.getSearchResult("MLB", queryText, offset)
            }
        }

        binding?.pagedListRecyclerView?.setOnRefreshListener {
            offset = 0
            viewModel.getSearchResult("MLB", queryText, offset)
        }

        searchAdapter.apply {
            getNotifyItemClick().subscribe {
                val item = it.second
                DetailActivity.launch(requireActivity(), item.id, item.seller, item.price.installments)
            }.addTo(disposables)
        }
        searchAdapter.hasFooter = true
    }

    fun searchResultItems(text: String) {
        queryText = text
        offset = 0
        binding?.pagedListRecyclerView.takeUnless { it?.isRefreshing == true }?.isRefreshing = false
        viewModel.getSearchResult("MLB", text, offset)
    }

    @Override
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable("listState", binding?.pagedListRecyclerView?.getRecyclerView()?.layoutManager?.onSaveInstanceState())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        disposables.clear()
    }
}
