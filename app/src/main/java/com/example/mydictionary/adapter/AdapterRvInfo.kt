package com.example.mydictionary.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydictionary.databinding.ItemRvInfoBinding
import com.example.mydictionary.databinding.RvItemBinding
import com.example.mydictionary.model.Definition
import com.example.mydictionary.model.Meaning
import com.example.mydictionary.model.My_Dictionary

class AdapterRvInfo(var list: List<Definition>) :
    RecyclerView.Adapter<AdapterRvInfo.ViewHolder>() {

    inner class ViewHolder(var itemBinding: ItemRvInfoBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(mydic: Definition) {
            itemBinding.apply {
                text.setText(mydic.definition)
                text2.setText(mydic.example)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRvInfo.ViewHolder {
        return ViewHolder(
            ItemRvInfoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])

    }

    override fun getItemCount(): Int = list.size


}