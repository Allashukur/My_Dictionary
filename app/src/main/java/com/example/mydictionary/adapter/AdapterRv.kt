package com.example.mydictionary.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mydictionary.databinding.RvItemBinding
import com.example.mydictionary.model.My_Dictionary

class AdapterRv(var list: List<My_Dictionary>, var onClickListener: onClick) :
    RecyclerView.Adapter<AdapterRv.ViewHolder>() {

    inner class ViewHolder(var itemBinding: RvItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun onBind(mydic: My_Dictionary) {
            itemBinding.apply {
                title.setText(mydic.word)
                if (mydic.phonetics.size > 1) {
                    text.setText("[ ${mydic.phonetics.get(1).text} ]")
                }
                text2.setText(mydic.meanings.get(0).partOfSpeech.toString())
                val definitions = mydic.meanings.get(0).definitions
                for (i in definitions) {
                    if (!i.definition.isNullOrEmpty() && !i.example.isNullOrEmpty()) {
                        definition.setText(i.definition)
                        example.setText(i.example)
                        break
                    }

                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterRv.ViewHolder {
        return ViewHolder(RvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(list[position])
        holder.itemBinding.nextPage.setOnClickListener {
            onClickListener.onClickListener(position)
        }
    }

    override fun getItemCount(): Int = list.size

    interface onClick {
        fun onClickListener(postion: Int)
    }
}