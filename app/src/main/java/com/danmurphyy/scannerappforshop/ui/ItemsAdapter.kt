package com.danmurphyy.scannerappforshop.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.danmurphyy.scannerappforshop.databinding.ListLayoutBinding
import com.danmurphyy.scannerappforshop.model.ModelItems

class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    private val itemsDiffUtil = object : DiffUtil.ItemCallback<ModelItems>() {
        override fun areItemsTheSame(oldItem: ModelItems, newItem: ModelItems): Boolean {
            return oldItem.itemBarCode == newItem.itemBarCode
        }

        override fun areContentsTheSame(oldItem: ModelItems, newItem: ModelItems): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, itemsDiffUtil)

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemModel = differ.currentList[position]
        val binding = holder.binding
        binding.viewitembarcode.text = itemModel.itemBarCode
        binding.viewitemname.text = itemModel.itemName
        binding.viewitemcategory.text = itemModel.itemCategory
        binding.viewitemprice.text = itemModel.itemPrice
    }

}