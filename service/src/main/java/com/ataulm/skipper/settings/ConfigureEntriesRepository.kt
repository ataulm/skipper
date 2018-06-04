package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.*

class ConfigureEntriesRepository(private val installedAppsService: InstalledAppsService, private val skipperPersistence: SkipperPersistence) {

    fun apps(): LiveData<List<App>> {
        return AppsLiveData(installedAppsService)
    }

    fun appsAssociatedWith(word: ClickableWord): LiveData<List<AppPackageName>> {
        return AssociatedAppsLiveData(skipperPersistence, word)
    }

    fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>) {
        skipperPersistence.updateAppsAssociatedWithWord(clickableWord, packageNames)
    }

    private class AppsLiveData(private val installedAppsService: InstalledAppsService) : MutableLiveData<List<App>>() {

        override fun onActive() {
            super.onActive()
            Thread({
                postValue(installedAppsService.getApps())
            }).start()
        }
    }

    private class AssociatedAppsLiveData(private val skipperPersistence: SkipperPersistence, private val word: ClickableWord) : MutableLiveData<List<AppPackageName>>() {

        override fun onActive() {
            super.onActive()
            value = skipperPersistence.appsAssociatedWith(word)
        }
    }
}
