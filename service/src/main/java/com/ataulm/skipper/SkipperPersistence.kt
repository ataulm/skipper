package com.ataulm.skipper

interface SkipperPersistence {

    @Deprecated("inverted - use targetedApps")
    fun appsAssociatedWith(clickableWord: ClickableWord): List<AppPackageName>

    @Deprecated("old - use persistAssociations")
    fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>)

    @Deprecated("inverted - use clickableWords(AppPackageName)")
    fun clickableWords(): List<ClickableWord>

    @Deprecated("old - use persistAssociations")
    fun add(clickableWord: ClickableWord)

    @Deprecated("old - use persistAssociations")
    fun delete(clickableWord: ClickableWord)

    fun targetedApps(): List<AppPackageName>

    fun clickableWords(app: AppPackageName): List<ClickableWord>

    fun persistAssociations(app: AppPackageName, vararg words: ClickableWord)

    fun addOnChangeListener(callback: SkipperPersistence.Callback)

    fun removeChangeListener(callback: SkipperPersistence.Callback)

    interface Callback {

        @Deprecated("old - use onDataChanged and requery")
        fun onChange(clickableWords: List<ClickableWord>)

        fun onDataChanged()
    }
}
