package br.com.bitsolutions.mercadolivre.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.bitsolutions.mercadolivre.databinding.DetailAttributeViewBinding
import br.com.bitsolutions.mercadolivre.domain.home.model.SearchItemAttribute

class DetailAttributeAdapter(private val items: List<SearchItemAttribute>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DetailAttributeViewHolder(
            DetailAttributeViewBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as DetailAttributeViewHolder

        viewHolder.tvDetailAttributeLabel.text = items[position].name
        viewHolder.tvDetailAttributeValue.text = items[position].value
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private class DetailAttributeViewHolder(itemView: DetailAttributeViewBinding) : RecyclerView.ViewHolder(itemView.root) {
        var tvDetailAttributeLabel: TextView = itemView.tvDetailAttributeLabel
        var tvDetailAttributeValue: TextView = itemView.tvDetailAttributeValue
    }
}
