package com.ataulm.skipper.settings

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class AppsAdapter(private val callback: Callback) : RecyclerView.Adapter<AppViewHolder>() {

    private var list: List<AppWordAssociations> = emptyList()

    fun update(list: List<AppWordAssociations>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        return AppViewHolder.inflate(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    interface Callback {

        fun onClick(appWordAssociations: AppWordAssociations)
    }
}
