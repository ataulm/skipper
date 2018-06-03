package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ataulm.skipper.ClickableWord
import com.example.R
import kotlinx.android.synthetic.main.item_clickable_word.view.*

class ClickableWordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun inflate(parent: ViewGroup): ClickableWordViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clickable_word, parent, false)
            return ClickableWordViewHolder(view)
        }
    }

    fun bind(clickableWord: ClickableWord, callback: ClickableWordsAdapter.Callback) {
        // TODO: set content description
        // TODO: bind associated packages
        itemView.setOnClickListener { callback.onClickConfigure(clickableWord) }
        itemView.clickableWordDeleteButton.setOnClickListener { callback.onClickDelete(clickableWord) }
        itemView.clickableWordTextView.text = clickableWord.word
    }
}
