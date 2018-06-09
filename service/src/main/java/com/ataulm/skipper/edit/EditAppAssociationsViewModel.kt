package com.ataulm.skipper.edit

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.SkipperRepository
import com.ataulm.skipper.observer.Event

class EditAppAssociationsViewModel(private val appPackageName: AppPackageName, private val repository: SkipperRepository) {

    private val eventsLiveData = MutableLiveData<Event<EditScreenEvent>>()

    fun data(): LiveData<List<ClickableWord>> {
        return repository.clickableWords(appPackageName)
    }

    fun events(): LiveData<Event<EditScreenEvent>> {
        return eventsLiveData
    }

    fun onClickAdd(word: ClickableWord) {
        repository.add(appPackageName, word)
    }

    fun onClickDelete(word: ClickableWord) {
        repository.delete(appPackageName, word)
    }

    fun onClickDismiss() {
        eventsLiveData.postValue(Event(EditScreenEvent.DismissEditScreen()))
    }

    sealed class EditScreenEvent {

        class DismissEditScreen : EditScreenEvent()
    }
}
