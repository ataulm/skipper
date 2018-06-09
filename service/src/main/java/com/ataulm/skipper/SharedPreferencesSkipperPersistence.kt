package com.ataulm.skipper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesSkipperPersistence private constructor(private val sharedPrefs: SharedPreferences) : SkipperPersistence {

    companion object {

        private const val FILE = "clickable_words"
        private const val KEY_TARGETED_APPS_LIST = "targeted_apps_list"

        fun create(context: Context): SharedPreferencesSkipperPersistence {
            val sharedPrefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            return SharedPreferencesSkipperPersistence(sharedPrefs)
        }
    }

    private val callbacks = mutableSetOf<SkipperPersistence.Callback>()

    override fun targetedApps(): List<AppPackageName> {
        return targetedAppsImmutable().map { AppPackageName(it) }
    }

    override fun clickableWords(app: AppPackageName): List<ClickableWord> {
        return sharedPrefs.getStringSet(app.packageName, emptySet()).map { ClickableWord(it) }
    }

    override fun add(app: AppPackageName, clickableWord: ClickableWord) {
        val set = sharedPrefs.getStringSet(app.packageName, emptySet()).toMutableSet()
        if (set.add(clickableWord.word)) {
            updateWordAssociationsForTargetedApp(app, set.map { ClickableWord(it) }.toTypedArray())
        }
    }

    override fun delete(app: AppPackageName, clickableWord: ClickableWord) {
        val set = sharedPrefs.getStringSet(app.packageName, emptySet()).toMutableSet()
        if (set.remove(clickableWord.word)) {
            updateWordAssociationsForTargetedApp(app, set.map { ClickableWord(it) }.toTypedArray())
        }
    }

    private fun updateWordAssociationsForTargetedApp(app: AppPackageName, words: Array<out ClickableWord>) {
        val editor = sharedPrefs.edit()
        val targetedApps = targetedAppsImmutable().toMutableSet()
        if (words.isEmpty()) {
            editor.remove(app.packageName)
            targetedApps.remove(app.packageName)
        } else {
            editor.putStringSet(app.packageName, words.map { it.word }.toSet())
            targetedApps.add(app.packageName)
        }
        editor.putStringSet(KEY_TARGETED_APPS_LIST, targetedApps)
        editor.apply()
    }

    override fun addOnChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.add(callback)
        if (callbacks.size == 1) {
            sharedPrefs.registerOnSharedPreferenceChangeListener(dataChangedCallback)
        }
    }

    override fun removeChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(dataChangedCallback)
        }
    }

    private val dataChangedCallback = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        callbacks.forEach({ it.onDataChanged() })
    }

    private fun targetedAppsImmutable() = sharedPrefs.getStringSet(KEY_TARGETED_APPS_LIST, emptySet())
}
