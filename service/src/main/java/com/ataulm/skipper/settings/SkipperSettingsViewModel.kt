package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.ataulm.skipper.App
import com.ataulm.skipper.AppsToWordsMap
import com.ataulm.skipper.SkipperRepository
import com.ataulm.skipper.observer.Event
import java.util.*

class SkipperSettingsViewModel(private val repository: SkipperRepository) : ViewModel() {

    private val eventsLiveData = MutableLiveData<Event<OpenConfigureAppEvent>>()

    fun configuredApps(): LiveData<List<AppWordAssociations>> {
        val mediatorLiveData = MediatorLiveData<List<AppWordAssociations>>()
        mediatorLiveData.addSource(repository.appsToWordsMap(), { associations ->
            mediatorLiveData.addSource(repository.installedApps(), { apps ->
                mediatorLiveData.value = combine(associations!!, apps!!)
            })
        })
        return mediatorLiveData
    }

    fun events(): LiveData<Event<OpenConfigureAppEvent>> {
        return eventsLiveData
    }

    private fun combine(associations: AppsToWordsMap, apps: List<App>): List<AppWordAssociations> {
        return apps
                .sortedBy { app -> app.name.toLowerCase(Locale.US) }
                .map { AppWordAssociations(it, associations.getOrDefault(it.packageName, emptyList())) }
                .sortedBy { it.associatedWords.isEmpty() }
    }

    fun onClick(appWordAssociations: AppWordAssociations) {
        eventsLiveData.value = Event(OpenConfigureAppEvent(appWordAssociations.app.packageName))
    }
}
