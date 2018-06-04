package com.ataulm.skipper.settings

import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperPersistence

class FakeSkipperPersistence(
        private val clickableWords: MutableList<ClickableWord> = emptyList<ClickableWord>().toMutableList(),
        private val appAssociations: MutableMap<ClickableWord, MutableList<AppPackageName>> = emptyMap<ClickableWord, MutableList<AppPackageName>>().toMutableMap()
) : SkipperPersistence {

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
