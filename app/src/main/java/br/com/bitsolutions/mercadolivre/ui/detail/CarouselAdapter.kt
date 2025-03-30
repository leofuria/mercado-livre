package br.com.bitsolutions.mercadolivre.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.bitsolutions.mercadolivre.R
import br.com.bitsolutions.mercadolivre.databinding.ImageViewBinding
import coil3.load
import coil3.request.CachePolicy
import coil3.request.error
import coil3.request.placeholder
import coil3.size.Scale

class CarouselAdapter(private val carouselDataList: ArrayList<String>) :
    RecyclerView.Adapter<CarouselAdapter.CarouselItemViewHolder>() {

    class CarouselItemViewHolder(view: ImageViewBinding) : RecyclerView.ViewHolder(view.root) {
        var ivResultImage = view.ivResultImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselItemViewHolder {
        val viewHolder = ImageViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CarouselItemViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: CarouselItemViewHolder, position: Int) {
        holder.ivResultImage.load(carouselDataList[position]) {
            placeholder(R.drawable.img_placeholder)
            error(R.drawable.img_placeholder)
            diskCachePolicy(CachePolicy.ENABLED)
            scale(Scale.FIT)
        }
    }

    override fun getItemCount(): Int {
        return carouselDataList.size
    }
}
