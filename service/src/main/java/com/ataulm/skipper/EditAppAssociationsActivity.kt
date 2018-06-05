package com.ataulm.skipper

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_app_associations.*

class EditAppAssociationsActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME"
    }

    private lateinit var viewModel: EditAppAssociationsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_app_associations)

        val appPackageName = AppPackageName(intent.getStringExtra(EXTRA_PACKAGE_NAME)) // TODO: should fetch App to get app title
        title = "Edit app \"${appPackageName.packageName}\"" // TODO: string resources
        val repository = SkipperRepository(InstalledAppsService(packageManager), SharedPreferencesSkipperPersistence.create(this))
        viewModel = EditAppAssociationsViewModel(appPackageName, repository)

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
    }

    override fun onResume() {
        super.onResume()
        viewModel.clickableWords().observe(this, Observer<List<ClickableWord>> { list ->
            if (list == null) {
                return@Observer
            }
            (configureEntriesPackagesRecyclerView.adapter as ClickableWordsAdapter).update(list)
        })
    }

    override fun onBackPressed() {
        // TODO: don't call super, use nav events
        super.onBackPressed()
        viewModel.onClickDismiss()
    }
}
