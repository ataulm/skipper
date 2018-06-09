package com.ataulm.skipper.edit

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.SkipperRepository

class EditAppViewModelProvider(private val appPackageName: AppPackageName, private val repository: SkipperRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EditAppViewModel(appPackageName, repository) as T
    }
}
