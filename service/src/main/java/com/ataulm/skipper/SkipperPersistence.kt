package com.ataulm.skipper

interface SkipperPersistence {

    fun appsAssociatedWith(clickableWord: ClickableWord): List<AppPackageName>

    fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>)

    fun clickableWords(): List<ClickableWord>

    fun add(clickableWord: ClickableWord)

    fun delete(clickableWord: ClickableWord)

    fun addOnChangeListener(callback: SkipperPersistence.Callback)

    fun removeChangeListener(callback: SkipperPersistence.Callback)

    interface Callback {

        fun onChange(clickableWords: List<ClickableWord>)
    }
}
