package br.com.bitsolutions.mercadolivre.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.FragmentFavoriteBinding
import br.com.bitsolutions.mercadolivre.domain.base.State
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import br.com.bitsolutions.mercadolivre.ui.detail.DetailActivity
import br.com.bitsolutions.mercadolivre.ui.home.SearchAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding

    val viewModel by viewModel<FavoriteViewModel>()
    private lateinit var searchAdapter: SearchAdapter
    protected val disposables = CompositeDisposable()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        val root: View = binding?.root!!

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.resultItems.collectLatest {
                        val isLoading = it.isLoading()
                        binding?.pagedListRecyclerView.takeUnless { it?.isRefreshing == !isLoading }?.isRefreshing = isLoading
                        when (it) {
                            is State.Data -> onSuccessFetchItems(it.data)
                            else -> Unit
                        }
                    }
                }
            }
        }

        return root
    }

    private fun onSuccessFetchItems(data: List<SearchResultItem>) {
        binding?.pagedListRecyclerView?.isRefreshing = false
        if (data.isEmpty() == true) {
            binding?.pagedListRecyclerView?.showFeedbackStatus(
                imageResource = R.drawable.ic_favorite_black_24dp,
                feedbackTitle = R.string.favorite_not_found_title,
                feedbackMessage = R.string.favorite_not_found_description,
            )
        } else {
            searchAdapter.submitList(data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        if (savedInstanceState != null) {
            binding?.pagedListRecyclerView?.getRecyclerView()?.layoutManager?.onRestoreInstanceState(savedInstanceState.getParcelable("listState"))
        }

        viewModel.getItemsList()

//        viewModel.resultItems.value.takeIf { it.isEmpty() }?.let {
//            binding?.pagedListRecyclerView?.showFeedbackStatus(
//                imageResource = R.drawable.ic_search_black_24dp,
//                feedbackTitle = R.string.paged_list_search_title,
//                feedbackMessage = R.string.paged_list_search_description,
//            )
//        }
    }

    private fun initAdapter() {
        searchAdapter = SearchAdapter { position, item ->
            removeFavoriteStatus(position, item)
        }
        binding?.pagedListRecyclerView?.layoutManager = LinearLayoutManager(requireContext())
        binding?.pagedListRecyclerView?.getRecyclerView()?.setHasFixedSize(true)
        binding?.pagedListRecyclerView?.adapter = this.searchAdapter
        searchAdapter.loadEnable = false
        binding?.pagedListRecyclerView?.isRefreshing = false

        searchAdapter.apply {
            getNotifyItemClick().subscribe {
                val item = it.second
                DetailActivity.launch(requireActivity(), item.id, item.seller, item.price.installments)
            }.addTo(disposables)
        }
        searchAdapter.hasFooter = true
    }

    private fun removeFavoriteStatus(position: Int, item: SearchResultItem) {
        viewModel.deleteItem(item)
        searchAdapter.getItem(position).isFavorite = false
        searchAdapter.removeItem(position)
        binding?.pagedListRecyclerView?.getRecyclerView()?.adapter?.notifyItemRemoved(position)
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
