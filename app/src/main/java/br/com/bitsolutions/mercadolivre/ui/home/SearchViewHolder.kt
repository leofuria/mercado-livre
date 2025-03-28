package br.com.bitsolutions.mercadolivre.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.SearchItemViewBinding
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchResultItem
import br.com.bitsolutions.pagedlist.adapter.PagedListViewHolder
import coil3.load
import coil3.request.CachePolicy
import coil3.request.placeholder
import coil3.size.Scale

class SearchViewHolder(private val view: SearchItemViewBinding) : PagedListViewHolder<SearchResultItem>(view.root) {

    companion object {
        fun create(parent: ViewGroup): SearchViewHolder {
            return SearchViewHolder(
                SearchItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            )
        }
    }

    init {
        itemView.setOnClickListener {
            currentItem?.run {
                notifyItemClickViewHolder.onNext(this)
            }
        }
    }

    override fun bindItem(item: SearchResultItem?) {
        currentItem = item
        currentItem?.let {
            view.tvResultTitle.text = it.title
            view.ivResultImage.load(it.thumbnail) {
                placeholder(R.drawable.img_placeholder)
                diskCachePolicy(CachePolicy.ENABLED)
                scale(Scale.FIT)
            }
            view.tvResultOriginalPrice.visibility = if (it.price.formatedOriginalAmount.isBlank()) View.GONE else View.VISIBLE
            view.tvResultOriginalPrice.text = it.price.formatedOriginalAmount
            view.tvResultPrice.text = it.price.formatedAmount
            it.price.installments?.let { installments ->
                view.tvResultInstallments.text = String.format("${installments.quantity}x ${installments.formatedAmount}")
            }
        }
    }
}
