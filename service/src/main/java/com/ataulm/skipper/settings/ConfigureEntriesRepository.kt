package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.App
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperSharedPrefs

class ConfigureEntriesRepository(private val installedAppsService: InstalledAppsService, private val sharedPrefs: SkipperSharedPrefs) {

    fun apps(): LiveData<List<App>> {
        return AppsLiveData(installedAppsService)
    }

    fun appsAssociatedWith(word: ClickableWord): LiveData<List<AppPackageName>> {
        return AssociatedAppsLiveData(sharedPrefs, word)
    }

    fun updateAppsAssociatedWithWord(clickableWord: ClickableWord, packageNames: Set<AppPackageName>) {
        sharedPrefs.updateAppsAssociatedWithWord(clickableWord, packageNames)
    }

    private class AppsLiveData(private val installedAppsService: InstalledAppsService) : MutableLiveData<List<App>>() {

        override fun onActive() {
            super.onActive()
            Thread({
                postValue(installedAppsService.getApps().sortedBy {app -> return@sortedBy app.name })
            }).start()
        }
    }

    private class AssociatedAppsLiveData(private val sharedPrefs: SkipperSharedPrefs, private val word: ClickableWord) : MutableLiveData<List<AppPackageName>>() {

        override fun onActive() {
            super.onActive()
            value = sharedPrefs.appsAssociatedWith(word)
        }
    }
}
