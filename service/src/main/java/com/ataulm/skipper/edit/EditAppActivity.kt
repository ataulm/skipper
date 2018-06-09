package com.ataulm.skipper.edit

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.ataulm.skipper.*
import com.ataulm.skipper.observer.DataObserver
import com.ataulm.skipper.observer.EventObserver
import com.ataulm.skipper.settings.SkipperViewModelProvider
import kotlinx.android.synthetic.main.activity_edit_app_associations.*

class EditAppActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"
    }

    private lateinit var viewModel: EditAppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app_associations)

        val appPackageName = AppPackageName(intent.getStringExtra(EXTRA_PACKAGE_NAME)) // TODO: should fetch App to get app title
        title = "Edit app \"${appPackageName.packageName}\"" // TODO: string resources
        val viewModelProvider = ViewModelProviders.of(this, EditAppViewModelProvider(appPackageName, SkipperRepository(InstalledAppsService(packageManager), injectPersistence())))
        viewModel = viewModelProvider.get(EditAppViewModel::class.java)

        wordsRecyclerView.adapter = ClickableWordsAdapter(object : ClickableWordsAdapter.Callback {

            override fun onClickDelete(word: ClickableWord) {
                viewModel.onClickDelete(word)
            }
        })

        addWordButton.setOnClickListener {
            val text = wordEditText.text
            if (text.isNotBlank()) {
                viewModel.onClickAdd(ClickableWord(text.toString()))
                wordEditText.text = null
            }
        }

        viewModel.events().observe(this, EventObserver(handleEvent()))
        viewModel.data().observe(this, DataObserver<List<ClickableWord>> { wordsRecyclerView.updateApps(it) })
    }

    private fun handleEvent(): (EditScreenEvent) -> Unit {
        return {
            when (it) {
                is EditScreenEvent.DismissEditScreen -> {
                    finish()
                }
            }
        }
    }

    override fun onBackPressed() {
        viewModel.onClickDismiss()
    }
}

private fun RecyclerView.updateApps(list: List<ClickableWord>) {
    (adapter as ClickableWordsAdapter).update(list)
}
