package com.ataulm.skipper

import android.arch.lifecycle.LiveData

class EditAppAssociationsViewModel(private val appPackageName: AppPackageName, private val repository: SkipperRepository) {

    fun clickableWords(): LiveData<List<ClickableWord>> {
        val clickableWords = repository.clickableWords(appPackageName)
        return clickableWords
    }

    fun onClickAdd(word: ClickableWord) {
        repository.add(appPackageName, word)
    }

    fun onClickDelete(word: ClickableWord) {
        repository.delete(appPackageName, word)
    }

    fun onClickDismiss() {
        // TODO: send close event
    }
}
