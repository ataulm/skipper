package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperPersistence

class ClickableWordsRepository(private val skipperPersistence: SkipperPersistence) {

    fun clickableWords(): LiveData<List<ClickableWord>> {
        return ClickableWordsLiveData(skipperPersistence)
    }

    fun add(clickableWord: ClickableWord) {
        skipperPersistence.add(clickableWord)
    }

    fun delete(clickableWord: ClickableWord) {
        skipperPersistence.delete(clickableWord)
    }

    class ClickableWordsLiveData(private val skipperPersistence: SkipperPersistence) : MutableLiveData<List<ClickableWord>>() {

        override fun onActive() {
            super.onActive()
            value = skipperPersistence.clickableWords()
            skipperPersistence.addOnChangeListener(callback)
        }

        override fun onInactive() {
            skipperPersistence.removeChangeListener(callback)
            super.onInactive()
        }

        private val callback = object : SkipperPersistence.Callback {

            override fun onDataChanged() {
                value = skipperPersistence.clickableWords()
            }

            override fun onChange(clickableWords: List<ClickableWord>) {
                value = clickableWords
            }
        }
    }
}
