package com.ataulm.skipper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesSkipperPersistence private constructor(private val sharedPrefs: SharedPreferences) : SkipperPersistence {

    companion object {

        private const val FILE = "clickable_words"
        private const val KEY_CLICKABLE_WORDS_LIST = "clickable_words_list"
        private const val KEY_TARGETED_APPS_LIST = "targeted_apps_list"

        fun create(context: Context): SharedPreferencesSkipperPersistence {
            val sharedPrefs = context.getSharedPreferences(FILE, Context.MODE_PRIVATE)
            return SharedPreferencesSkipperPersistence(sharedPrefs)
        }
    }

    private val callbacks = mutableSetOf<SkipperPersistence.Callback>()

    override fun appsAssociatedWith(clickableWord: ClickableWord): List<AppPackageName> {
        return sharedPrefs.getStringSet(clickableWord.word, emptySet()).toList().map { AppPackageName(it) }
    }

    override fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>) {
        val packageNamesStringSet = packageNames.map { it.packageName }.toSet()
        sharedPrefs.edit().putStringSet(clickableWord.word, packageNamesStringSet).apply()
    }

    override fun clickableWords(): List<ClickableWord> {
        return immutableWords().toList().map { ClickableWord(it) }
    }

    override fun add(clickableWord: ClickableWord) {
        val set = immutableWords().toMutableSet()
        set.add(clickableWord.word)
        persistClickableWords(set.toList())
    }

    override fun delete(clickableWord: ClickableWord) {
        val list = immutableWords().toMutableList()
        list.remove(clickableWord.word)
        persistClickableWords(list)

        removeAppsAssociatedWith(clickableWord)
    }

    private fun removeAppsAssociatedWith(clickableWord: ClickableWord) {
        sharedPrefs.edit().remove(clickableWord.word).apply()
    }

    private fun immutableWords() = sharedPrefs.getStringSet(KEY_CLICKABLE_WORDS_LIST, emptySet())

    private fun persistClickableWords(list: List<String>) {
        sharedPrefs.edit().putStringSet(KEY_CLICKABLE_WORDS_LIST, list.toSet()).apply()
    }

    override fun targetedApps(): List<AppPackageName> {
        return targetedAppsImmutable().map { AppPackageName(it) }
    }

    override fun clickableWords(app: AppPackageName): List<ClickableWord> {
        return sharedPrefs.getStringSet(app.packageName, emptySet()).map { ClickableWord(it) }
    }

    override fun persistAssociations(app: AppPackageName, vararg words: ClickableWord) {
        if (words.isEmpty()) {
            removeTargetedApp(app)
        } else {
            updateWordAssociationsForTargetedApp(app, words)
        }
    }

    private fun removeTargetedApp(app: AppPackageName) {
        val targetedApps = targetedAppsImmutable().toMutableSet()
        targetedApps.remove(app.packageName)
        sharedPrefs.edit()
                .remove(app.packageName) // remove associated words for this app
                .putStringSet(KEY_TARGETED_APPS_LIST, targetedApps)
                .apply()
    }

    private fun updateWordAssociationsForTargetedApp(app: AppPackageName, words: Array<out ClickableWord>) {
        val targetedApps = targetedAppsImmutable().toMutableSet()
        targetedApps.add(app.packageName)
        sharedPrefs.edit()
                .putStringSet(KEY_TARGETED_APPS_LIST, targetedApps)
                .putStringSet(app.packageName, words.map { it.word }.toSet())
                .apply()
    }

    override fun addOnChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.add(callback)
        if (callbacks.size == 1) {
            sharedPrefs.registerOnSharedPreferenceChangeListener(targetedAppsChangedCallback)
            sharedPrefs.registerOnSharedPreferenceChangeListener(clickableWordsChangedListener)
        }
    }

    override fun removeChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(targetedAppsChangedCallback)
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(clickableWordsChangedListener)
        }
    }

    private val clickableWordsChangedListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        if (key == KEY_CLICKABLE_WORDS_LIST) {
            val clickableWords = clickableWords()
            callbacks.forEach({
                it.onChange(clickableWords)
            })
        }
    }

    private val targetedAppsChangedCallback = SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
        callbacks.forEach({ it.onDataChanged() })
    }

    private fun targetedAppsImmutable() = sharedPrefs.getStringSet(KEY_TARGETED_APPS_LIST, emptySet())
}
