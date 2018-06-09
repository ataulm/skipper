package com.ataulm.skipper

interface SkipperPersistence {

    fun targetedApps(): List<AppPackageName>

    fun clickableWords(app: AppPackageName): List<ClickableWord>

    fun add(app: AppPackageName, clickableWord: ClickableWord)

    fun delete(app: AppPackageName, clickableWord: ClickableWord)

    fun addOnChangeListener(callback: SkipperPersistence.Callback)

    fun removeChangeListener(callback: SkipperPersistence.Callback)

    interface Callback {

        fun onDataChanged()
    }
}
