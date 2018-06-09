package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ataulm.skipper.App
import com.ataulm.skipper.AppsToWordsMap
import com.ataulm.skipper.SkipperRepository
import com.ataulm.skipper.observer.DataObserver
import com.ataulm.skipper.observer.Event
import java.util.*

class SkipperSettingsViewModel(private val repository: SkipperRepository) : ViewModel() {

    fun apps(): LiveData<List<AppWordAssociations>> {
        return appWordAssociations
    }

    private val appWordAssociations = object : MediatorLiveData<List<AppWordAssociations>>() {
        override fun onActive() {
            super.onActive()
            val installedAppsLiveData = repository.installedApps()
            addSource(installedAppsLiveData, DataObserver<List<App>>({ installedApps ->
                removeSource(installedAppsLiveData)
                addSource(repository.appsToWordsMap(), DataObserver<AppsToWordsMap> { appsToWordsMap ->
                    value = combine(appsToWordsMap, installedApps)
                })
            }))
        }
    }

    private fun combine(appsToWordsMap: AppsToWordsMap, apps: List<App>): List<AppWordAssociations> {
        return apps
                .sortedBy { app -> app.name.toLowerCase(Locale.US) }
                .map { AppWordAssociations(it, appsToWordsMap.getOrDefault(it.packageName, emptyList())) }
                .sortedBy { it.associatedWords.isEmpty() }
    }

    fun events(): LiveData<Event<OpenConfigureAppEvent>> {
        return eventsLiveData
    }

    private val eventsLiveData = MutableLiveData<Event<OpenConfigureAppEvent>>()

    fun onUserClickApp(appWordAssociations: AppWordAssociations) {
        eventsLiveData.value = Event(OpenConfigureAppEvent(appWordAssociations.app.packageName))
    }
}
