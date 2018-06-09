package com.ataulm.skipper

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData

class SkipperRepository(private val installedAppsService: InstalledAppsService, private val skipperPersistence: SkipperPersistence) {

    // TODO: this isn't so much live since it will never send updates when new apps are installed
    // is there a way to mark it as "always request new data on subscribe"?
    fun installedApps(): LiveData<List<App>> {
        return InstalledAppsLiveData(installedAppsService)
    }

    fun appsToWordsMap(): LiveData<AppsToWordsMap> {
        return TargetedAppsToClickableWordsMap(skipperPersistence)
    }

    fun clickableWords(app: AppPackageName): LiveData<List<ClickableWord>> {
        return ClickableWordsLiveData(skipperPersistence, app)
    }

    fun add(app: AppPackageName, word: ClickableWord) {
        skipperPersistence.add(app, word)
    }

    fun delete(app: AppPackageName, word: ClickableWord) {
        skipperPersistence.delete(app, word)
    }

    private class InstalledAppsLiveData(private val installedAppsService: InstalledAppsService) : MutableLiveData<List<App>>() {

        override fun onActive() {
            super.onActive()
            Thread({ postValue(installedAppsService.getApps()) }).start()
        }
    }

    private class TargetedAppsToClickableWordsMap(private val persistence: SkipperPersistence) : MutableLiveData<AppsToWordsMap>() {

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

            override fun onDataChanged() {
                value = fetchAppsToWordsMapping()
            }
        }
    }

    private class ClickableWordsLiveData(private val persistence: SkipperPersistence, private val appPackageName: AppPackageName) : MutableLiveData<List<ClickableWord>>() {

        override fun onActive() {
            super.onActive()
            value = persistence.clickableWords(appPackageName)
            persistence.addOnChangeListener(callback)
        }

        override fun onInactive() {
            persistence.removeChangeListener(callback)
            super.onInactive()
        }

        private val callback = object : SkipperPersistence.Callback {

            override fun onDataChanged() {
                value = persistence.clickableWords(appPackageName)
            }
        }
    }
}
