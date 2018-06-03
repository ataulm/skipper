package com.ataulm.skipper.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.SkipperPersistence

class SkipperRepository(private val skipperPersistence: SkipperPersistence) {

    fun clickableWords(): LiveData<ClickableWords> {
        return ClickableWordsLiveData(skipperPersistence)
    }

    private class ClickableWordsLiveData(private val skipperPersistence: SkipperPersistence) : MutableLiveData<ClickableWords>() {

        override fun onActive() {
            super.onActive()
            val packageNamesToWordsMap = mutableMapOf<String, MutableSet<String>>()
            val clickableWords = skipperPersistence.clickableWords()
            clickableWords.forEach { clickableWord ->
                skipperPersistence.appsAssociatedWith(clickableWord).forEach { packageName ->
                    val set = packageNamesToWordsMap.getOrPut(packageName.packageName, { mutableSetOf() })
                    set.add(clickableWord.word)
                }
            }
            value = ClickableWords(packageNamesToWordsMap)
        }
    }
}
