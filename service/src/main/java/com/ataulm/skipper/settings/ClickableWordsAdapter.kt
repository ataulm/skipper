package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ataulm.skipper.ClickableWord
import kotlinx.android.synthetic.main.item_clickable_word.view.*

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
        holder.itemView.setOnClickListener { callback.onClickConfigure(clickableWord) }
        holder.itemView.clickableWordDeleteButton.setOnClickListener { callback.onClickDelete(clickableWord) }
        holder.itemView.clickableWordTextView.text = clickableWord.word
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    interface Callback {

        fun onClickDelete(word: ClickableWord)

        fun onClickConfigure(word: ClickableWord)
    }
}
