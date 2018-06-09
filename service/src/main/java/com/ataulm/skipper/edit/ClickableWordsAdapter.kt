package com.ataulm.skipper.edit

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ataulm.skipper.ClickableWord

class ClickableWordsAdapter(private val callback: Callback) : RecyclerView.Adapter<ClickableWordViewHolder>() {

    private var list: List<ClickableWord> = emptyList()

    fun update(list: List<ClickableWord>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClickableWordViewHolder {
        return ClickableWordViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ClickableWordViewHolder, position: Int) {
        val clickableWord = list[position]
        holder.bind(clickableWord, callback)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface Callback {

        fun onClickDelete(word: ClickableWord)
    }
}
