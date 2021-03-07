package com.example.sparktest.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.sparktest.R
import com.example.sparktest.data.model.Image
import com.example.sparktest.databinding.ItemImageBinding
import com.example.sparktest.util.OnListItemClickListener

/**
 * Adapter class for Gallery recyclerview
 */
class ImageAdapter : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {
    var data: List<Image> = ArrayList(1)
    lateinit var onClick: OnListItemClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ViewDataBinding>(
            inflater,
            R.layout.item_image,
            parent,
            false
        ) as ItemImageBinding
        return ImageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class ImageViewHolder(itemBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        var rowBinding = itemBinding as ItemImageBinding
        fun bind(
            threadDetails: Image,
            onClick: OnListItemClickListener?
        ) {
            rowBinding.data = threadDetails
            rowBinding.callBack = onClick
        }

    }

    fun setAdapterData(items: List<Image>) {
        data = items
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        if (data.isNotEmpty()) {
            holder.bind(data[position], onClick)
        }
    }

}