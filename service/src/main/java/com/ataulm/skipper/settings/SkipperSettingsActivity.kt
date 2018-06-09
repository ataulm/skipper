package com.ataulm.skipper.settings

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.ataulm.skipper.InstalledAppsService
import com.ataulm.skipper.R
import com.ataulm.skipper.SkipperRepository
import com.ataulm.skipper.edit.EditAppActivity
import com.ataulm.skipper.injectPersistence
import com.ataulm.skipper.observer.DataObserver
import com.ataulm.skipper.observer.EventObserver
import kotlinx.android.synthetic.main.activity_configure_apps.*

class SkipperSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_apps)

        val viewModelProvider = ViewModelProviders.of(this, SkipperViewModelProvider(SkipperRepository(InstalledAppsService(packageManager), injectPersistence())))
        val viewModel = viewModelProvider.get(SkipperSettingsViewModel::class.java)

        appsRecyclerView.adapter = AppsAdapter(object : AppsAdapter.Callback {
            override fun onClick(appWordAssociations: AppWordAssociations) {
                viewModel.onUserClickApp(appWordAssociations)
            }
        })

        viewModel.events().observe(this, EventObserver { handleEvent() })
        viewModel.apps().observe(this, DataObserver { appsRecyclerView.updateApps(it) })
    }

    private fun handleEvent(): (OpenEditScreenEvent) -> Unit {
        return {
            val intent = Intent(this@SkipperSettingsActivity, EditAppActivity::class.java)
                    .putExtra(EditAppActivity.EXTRA_PACKAGE_NAME, it.app.packageName)
            startActivity(intent)
        }
    }
}

private fun RecyclerView.updateApps(list: List<AppWordAssociations>) {
    (adapter as AppsAdapter).update(list)
}
