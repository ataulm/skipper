package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.ataulm.skipper.AppPackageName

class PackagesAdapter(private val callback: Callback) : RecyclerView.Adapter<PackageViewHolder>() {

    private var list: List<WordToAppAssociation> = emptyList()

    fun update(list: List<WordToAppAssociation>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PackageViewHolder {
        return PackageViewHolder.inflate(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PackageViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    interface Callback {

        fun onChangePackageCheckState(packageName: AppPackageName, checked: Boolean)
    }
}
