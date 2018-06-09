package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ataulm.skipper.R
import kotlinx.android.synthetic.main.item_configured_app.view.*

class AppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun inflate(parent: ViewGroup): AppViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_configured_app, parent, false)
            return AppViewHolder(view)
        }
    }

    fun bind(appWordAssociations: AppWordAssociations, callback: AppsAdapter.Callback) {
        itemView.appIconImageView.setImageDrawable(appWordAssociations.app.icon)
        itemView.appNameTextView.text = appWordAssociations.app.name
        itemView.statusImageView.setImageResource(
                if (appWordAssociations.associatedWords.isNotEmpty()) R.drawable.ic_active else R.drawable.ic_inactive
        )
        val statusContentDescription = if (appWordAssociations.associatedWords.isNotEmpty()) R.string.content_desc_active else R.string.content_desc_inactive
        itemView.statusImageView.contentDescription = itemView.resources.getString(statusContentDescription)
        itemView.setOnClickListener {
            callback.onClick(appWordAssociations)
        }
    }
}
