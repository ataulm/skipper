package com.ataulm.skipper.settings

import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperPersistence

class FakeSkipperPersistence(
        private val clickableWords: MutableList<ClickableWord> = emptyList<ClickableWord>().toMutableList(),
        private val appAssociations: MutableMap<ClickableWord, MutableList<AppPackageName>> = emptyMap<ClickableWord, MutableList<AppPackageName>>().toMutableMap(),
        private val targetedApps: MutableList<AppPackageName> = emptyList<AppPackageName>().toMutableList(),
        private val appToWordsMap: MutableMap<AppPackageName, MutableList<ClickableWord>> = emptyMap<AppPackageName, MutableList<ClickableWord>>().toMutableMap()
) : SkipperPersistence {

    override fun targetedApps(): List<AppPackageName> {
        return targetedApps
    }

    override fun clickableWords(app: AppPackageName): List<ClickableWord> {
        return appToWordsMap.getOrDefault(app, emptyList<ClickableWord>().toMutableList())
    }

    override fun persistAssociations(app: AppPackageName, vararg words: ClickableWord) {
        appToWordsMap.put(app, words.toMutableList())
        callbacks.forEach { it.onDataChanged() }
    }

    private val callbacks = mutableListOf<SkipperPersistence.Callback>()

    override fun appsAssociatedWith(clickableWord: ClickableWord): List<AppPackageName> {
        return appAssociations.getOrDefault(clickableWord, emptyList<AppPackageName>().toMutableList())
    }

    override fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>) {
        appAssociations[clickableWord] = packageNames.toMutableList()
    }

    override fun clickableWords(): List<ClickableWord> {
        return clickableWords
    }

    override fun add(clickableWord: ClickableWord) {
        if (!clickableWords.contains(clickableWord)) {
            clickableWords.add(clickableWord)
            callbacks.forEach({ it.onChange(clickableWords) })
        }
    }

    override fun delete(clickableWord: ClickableWord) {
        clickableWords.remove(clickableWord)
        callbacks.forEach({ it.onChange(clickableWords) })
    }

    override fun addOnChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.add(callback)
    }

    override fun removeChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.remove(callback)
    }
}
