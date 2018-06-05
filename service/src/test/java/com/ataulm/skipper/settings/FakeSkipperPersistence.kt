package com.ataulm.skipper.settings

import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperPersistence

class FakeSkipperPersistence(
        private val targetedApps: MutableList<AppPackageName> = emptyList<AppPackageName>().toMutableList(),
        private val appToWordsMap: MutableMap<AppPackageName, MutableList<ClickableWord>> = emptyMap<AppPackageName, MutableList<ClickableWord>>().toMutableMap()
) : SkipperPersistence {

    override fun add(app: AppPackageName, clickableWord: ClickableWord) {
        val set = appToWordsMap.getOrDefault(app, mutableListOf()).toMutableSet()
        if (set.add(clickableWord)) {
            appToWordsMap.put(app, set.toMutableList())
            callbacks.forEach { it.onDataChanged() }
        }
    }

    override fun delete(app: AppPackageName, clickableWord: ClickableWord) {
        val list = appToWordsMap.getOrDefault(app, mutableListOf())
        if (list.remove(clickableWord)) {
            appToWordsMap.put(app, list)
            callbacks.forEach { it.onDataChanged() }
        }
    }

    override fun targetedApps(): List<AppPackageName> {
        return targetedApps
    }

    override fun clickableWords(app: AppPackageName): List<ClickableWord> {
        return appToWordsMap.getOrDefault(app, emptyList<ClickableWord>().toMutableList())
    }

    private val callbacks = mutableListOf<SkipperPersistence.Callback>()

    override fun addOnChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.add(callback)
    }

    override fun removeChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.remove(callback)
    }
}
