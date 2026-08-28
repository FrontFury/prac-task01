package com.example.tasin_23_rest01.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tasin_23_rest01.databinding.ItemProductBinding
import com.example.tasin_23_rest01.model.Product

class ProductAdapter(
    private var productList: List<Product>
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {

            binding.tvProductTitle.text = product.title

            binding.tvProductPrice.text =
                "$${product.price}"

            binding.tvProductCategory.text =
                product.category?.name ?: "Unknown Category"

            binding.tvProductDescription.text =
                product.description ?: "No description available"

            val imageUrl = product.images
                ?.firstOrNull()
                ?.replace("[", "")
                ?.replace("]", "")
                ?.replace("\"", "")
                ?.trim()

            Glide.with(binding.root.context)
                .load(imageUrl)
                .centerCrop()
                .into(binding.ivProduct)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ProductViewHolder {

        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ProductViewHolder,
        position: Int
    ) {
        holder.bind(productList[position])
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun updateData(newList: List<Product>) {
        productList = newList
        notifyDataSetChanged()
    }
}