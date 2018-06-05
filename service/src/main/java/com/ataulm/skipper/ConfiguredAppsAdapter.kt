package com.ataulm.skipper

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

class ConfiguredAppsAdapter(private val callback: Callback) : RecyclerView.Adapter<ConfiguredAppViewHolder>() {

    private var list: List<ConfiguredApp> = emptyList()

    fun update(list: List<ConfiguredApp>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConfiguredAppViewHolder {
        return ConfiguredAppViewHolder.inflate(parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ConfiguredAppViewHolder, position: Int) {
        holder.bind(list[position], callback)
    }

    interface Callback {

        fun onClick(configuredApp: ConfiguredApp)
    }
}
