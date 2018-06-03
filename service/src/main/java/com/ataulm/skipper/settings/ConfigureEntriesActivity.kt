package com.ataulm.skipper.settings

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ataulm.skipper.AppPackageName
import com.ataulm.skipper.ClickableWord
import com.ataulm.skipper.InstalledAppsService
import com.ataulm.skipper.SharedPreferencesSkipperPersistence
import com.example.R
import kotlinx.android.synthetic.main.activity_configure_entries.*

class ConfigureEntriesActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_CLICKABLE_WORD = "EXTRA_CLICKABLE_WORD"
    }

    private lateinit var viewModel: ConfigureEntriesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_entries)

        val clickableWord = ClickableWord(intent.getStringExtra(EXTRA_CLICKABLE_WORD))
        title = "Configure \"${clickableWord.word}\"" // TODO: string resources
        val repository = ConfigureEntriesRepository(InstalledAppsService(packageManager), SharedPreferencesSkipperPersistence.create(this))
        viewModel = ConfigureEntriesViewModel(clickableWord, repository)

        configureEntriesPackagesRecyclerView.adapter = PackagesAdapter(object : PackagesAdapter.Callback {

            override fun onChangePackageCheckState(packageName: AppPackageName, checked: Boolean) {
                viewModel.onChangePackageCheckState(packageName, checked)
            }
        })
        configureEntriesSaveButton.setOnClickListener { viewModel.onClickSave() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.viewData().observe(this, Observer<List<WordToAppAssociation>> { list ->
            if (list == null) {
                return@Observer
            }
            (configureEntriesPackagesRecyclerView.adapter as PackagesAdapter).update(list)
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewModel.onClickDismiss()
    }
}
