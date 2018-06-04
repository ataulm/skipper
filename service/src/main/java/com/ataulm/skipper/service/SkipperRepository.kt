package com.ataulm.skipper.service

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperPersistence

class SkipperRepository(private val skipperPersistence: SkipperPersistence) {

    fun appsToWordsMap(): LiveData<AppsToWordsMap> {
        return ClickableWordsLiveData(skipperPersistence)
    }

    private class ClickableWordsLiveData(private val persistence: SkipperPersistence) : MutableLiveData<AppsToWordsMap>() {

        override fun onActive() {
            super.onActive()
            value = fetchAppsToWordsMapping()
            persistence.addOnChangeListener(callback)
        }

        private fun fetchAppsToWordsMapping(): MutableMap<AppPackageName, List<ClickableWord>> {
            val appsToWordsMap = mutableMapOf<AppPackageName, List<ClickableWord>>()
            persistence.targetedApps()
                    .forEach { appsToWordsMap[it] = persistence.clickableWords(it) }
            return appsToWordsMap
        }

        override fun onInactive() {
            persistence.removeChangeListener(callback)
            super.onInactive()
        }

        private val callback = object : SkipperPersistence.Callback {

            override fun onChange(clickableWords: List<ClickableWord>) {}

            override fun onDataChanged() {
                value = fetchAppsToWordsMapping()
            }
        }
    }
}
