package com.ataulm.skipper.settings

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord

class ConfigureEntriesViewModel(private val clickableWord: ClickableWord, private val repository: ConfigureEntriesRepository) {

    private val checkedPackages: MutableSet<AppPackageName> = mutableSetOf()

    fun viewData(): LiveData<List<WordToAppAssociation>> {
        val mediatorLiveData = MediatorLiveData<List<WordToAppAssociation>>()
        mediatorLiveData.addSource(repository.apps(), { apps ->
            mediatorLiveData.addSource(repository.appsAssociatedWith(clickableWord), { packages ->
                mediatorLiveData.value = apps!!
                        .sortedBy { app -> app.name }
                        .map { WordToAppAssociation(it, packages!!.contains(it.packageName)) }
                        .sortedByDescending { wordToAppAssociation -> wordToAppAssociation.associatedToWord }
            })
        })
        return mediatorLiveData
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
