package com.ataulm.skipper.edit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.ataulm.skipper.*
import com.ataulm.skipper.observer.DataObserver
import com.ataulm.skipper.observer.EventObserver
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
        val repository = SkipperRepository(InstalledAppsService(packageManager), injectPersistence())
        viewModel = EditAppViewModel(appPackageName, repository)

        configureEntriesPackagesRecyclerView.adapter = ClickableWordsAdapter(object : ClickableWordsAdapter.Callback {

            override fun onClickDelete(word: ClickableWord) {
                viewModel.onClickDelete(word)
            }
        })

        configureEntriesAddWordButton.setOnClickListener {
            val text = configureEntriesEditText.text
            if (text.isNotBlank()) {
                viewModel.onClickAdd(ClickableWord(text.toString()))
                configureEntriesEditText.text = null
            }
        }

        viewModel.events().observe(this, EventObserver {
            finish()
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.data().observe(this, DataObserver<List<ClickableWord>> {
            configureEntriesPackagesRecyclerView.updateApps(it)
        })
    }

    override fun onBackPressed() {
        viewModel.onClickDismiss()
    }

    private fun RecyclerView.updateApps(list: List<ClickableWord>) {
        (adapter as ClickableWordsAdapter).update(list)
    }
}
