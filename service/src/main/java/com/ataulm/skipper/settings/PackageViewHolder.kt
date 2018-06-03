package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.R
import kotlinx.android.synthetic.main.item_package.view.*

class PackageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {

        fun inflate(parent: ViewGroup): PackageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_package, parent, false)
            return PackageViewHolder(view)
        }
    }

    fun bind(wordToAppAssociation: WordToAppAssociation, callback: PackagesAdapter.Callback) {
        // TODO: set content description
        itemView.packageIconImageView.setImageDrawable(wordToAppAssociation.app.icon)
        itemView.packageNameTextView.text = wordToAppAssociation.app.name
        itemView.packageCheckBox.isChecked = wordToAppAssociation.associatedToWord
        itemView.packageCheckBox.setOnCheckedChangeListener({checkBoxView, isChecked ->
            callback.onChangePackageCheckState(wordToAppAssociation.app.packageName, isChecked)
        })
    }
}
