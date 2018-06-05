package com.ataulm.skipper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_configured_app.view.*

class ConfiguredAppViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun inflate(parent: ViewGroup): ConfiguredAppViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_configured_app, parent, false)
            return ConfiguredAppViewHolder(view)
        }
    }

    fun bind(app: ConfiguredApp, callback: ConfiguredAppsAdapter.Callback) {
        // TODO: set content description
        itemView.appIconImageView.setImageDrawable(app.app.icon)
        itemView.appNameTextView.text = app.app.name
        itemView.statusImageView.setImageResource(
                if (app.clickableWords.isNotEmpty()) R.drawable.ic_active else R.drawable.ic_inactive
        )
        val statusContentDescription = if (app.clickableWords.isNotEmpty()) R.string.content_desc_active else R.string.content_desc_inactive
        itemView.statusImageView.contentDescription = itemView.resources.getString(statusContentDescription)
        itemView.setOnClickListener {
            callback.onClick(app)
        }
    }
}
