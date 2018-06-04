package com.ataulm.skipper

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesSkipperPersistence private constructor(private val sharedPrefs: SharedPreferences) : SkipperPersistence {

    companion object {

        private const val FILE = "clickable_words"
        private const val KEY_CLICKABLE_WORDS_LIST = "clickable_words_list"

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

    override fun addOnChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.add(callback)
        if (callbacks.size == 1) {
            sharedPrefs.registerOnSharedPreferenceChangeListener(clickableWordsChangedListener)
        }
    }

    override fun removeChangeListener(callback: SkipperPersistence.Callback) {
        callbacks.remove(callback)
        if (callbacks.isEmpty()) {
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
}
