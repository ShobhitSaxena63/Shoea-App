package com.example.shoea.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shoea.R
import com.example.shoea.data.Product
import com.example.shoea.databinding.ProductsRvBackgroundBinding
import timber.log.Timber

class ProductListAdapter(val listener:(Long)->Unit):ListAdapter<Product,ProductListAdapter.ViewHolder>(DiffCallBack()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductsRvBackgroundBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

   inner class ViewHolder(private val binding: ProductsRvBackgroundBinding):RecyclerView.ViewHolder(binding.root) {
       init {
           itemView.setOnClickListener {
               listener.invoke(getItem(adapterPosition).id)
           }
       }
       fun bind(item : Product) {
           Glide.with(binding.root)
               .load(item.thumbnail)
               .error(R.drawable.ic_launcher_background)
               .into(binding.coverImage)
           Timber.d("Adapter Running from ViewHolder $item")
           binding.title.text = item.title.replaceFirstChar(Char::titlecase)
           binding.category.text = item.category.replaceFirstChar(Char::titlecase)
           binding.remaining.text = String.format(itemView.context.getString(R.string.product_left),item.stock)
           binding.brand.text = String.format(itemView.context.getString(R.string.brand_name),item.brand)
           binding.price.text = String.format(itemView.context.getString(R.string.price),item.price)

       }

   }

}

class DiffCallBack: DiffUtil.ItemCallback<Product>(){
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem==newItem
    }

}