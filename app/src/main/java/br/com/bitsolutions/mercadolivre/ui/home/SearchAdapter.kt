package br.com.bitsolutions.mercadolivre.ui.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import br.com.bitsolutions.pagedlist.adapter.PagedListAdapter
import io.reactivex.rxkotlin.addTo

class SearchAdapter(
    private val onFavoriteItemClick: (Int, SearchResultItem) -> Unit,
) : PagedListAdapter<SearchResultItem, SearchViewHolder>(DiffCallback) {

    companion object {
        val DiffCallback = object : DiffUtil.ItemCallback<SearchResultItem>() {
            override fun areItemsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SearchResultItem, newItem: SearchResultItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun bindItemView(holder: SearchViewHolder, position: Int) {
        holder.bindItem(getItem(position))
    }

    override fun createItemView(parent: ViewGroup): SearchViewHolder {
        return SearchViewHolder.create(parent, onFavoriteItemClick).apply {
            getNotifyItemClickViewHolder().subscribe {
                notifyItemClick.onNext(Pair(adapterPosition, it))
            }.addTo(disposable)
        }
    }
}
