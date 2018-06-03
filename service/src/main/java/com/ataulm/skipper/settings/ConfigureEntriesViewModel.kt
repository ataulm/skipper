package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.ataulm.skipper.App
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import java.util.*

class ConfigureEntriesViewModel(private val clickableWord: ClickableWord, private val repository: ConfigureEntriesRepository) {

    private val checkedPackages: MutableSet<AppPackageName> = mutableSetOf()

    fun viewData(): LiveData<List<WordToAppAssociation>> {
        val mediatorLiveData = MediatorLiveData<List<WordToAppAssociation>>()
        mediatorLiveData.addSource(repository.apps(), { apps ->
            mediatorLiveData.addSource(repository.appsAssociatedWith(clickableWord), { packages ->
                mediatorLiveData.value = combine(apps!!, packages!!)
            })
        })
        return mediatorLiveData
    }

    private fun combine(apps: List<App>, packages: List<AppPackageName>): List<WordToAppAssociation> {
        return apps
                .sortedBy { app -> app.name.toLowerCase(Locale.US) }
                .map { WordToAppAssociation(it, packages.contains(it.packageName)) }
                .sortedByDescending { wordToAppAssociation -> wordToAppAssociation.associatedToWord }
    }

    fun onChangePackageCheckState(packageName: AppPackageName, checked: Boolean) {
        if (checked) {
            checkedPackages.add(packageName)
        } else {
            checkedPackages.remove(packageName)
        }
    }

    fun onClickSave() {
        repository.updateAppsAssociatedWithWord(clickableWord, checkedPackages)
        sendCloseEvent()
    }

    fun onClickDismiss() {
        checkedPackages.clear()
        sendCloseEvent()
    }

    private fun sendCloseEvent() {
        // TODO: model navigation events to which the view can subscribe
    }
}
