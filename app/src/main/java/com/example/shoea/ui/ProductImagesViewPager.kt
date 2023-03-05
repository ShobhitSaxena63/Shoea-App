package com.example.shoea.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoea.R
import com.example.shoea.data.Product
import com.example.shoea.databinding.ProductImagesViewpagerBackgroundBinding

class ProductImagesViewPager:ListAdapter<String, ProductImagesViewPager.ViewHolder>(DiffCallBacks()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductImagesViewpagerBackgroundBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class ViewHolder(private val binding: ProductImagesViewpagerBackgroundBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(product:String){
            Glide.with(binding.root)
                .load(product)
                .error(R.drawable.ic_launcher_background)
                .into(binding.productImage)

        }
    }
}

class DiffCallBacks : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
