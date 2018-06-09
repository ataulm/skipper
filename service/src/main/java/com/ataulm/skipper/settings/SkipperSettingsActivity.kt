package com.ataulm.skipper.settings

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import com.ataulm.skipper.*
import com.ataulm.skipper.observer.DataObserver
import com.ataulm.skipper.observer.EventObserver
import kotlinx.android.synthetic.main.activity_configure_apps.*

class SkipperSettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_apps)

        // TODO: move this to viewmodel provider otherwise we don't get benefits of viewmodel
        val viewModel = SkipperSettingsViewModel(SkipperRepository(InstalledAppsService(packageManager), injectPersistence()))

        appsRecyclerView.adapter = AppsAdapter(object : AppsAdapter.Callback {
            override fun onClick(appWordAssociations: AppWordAssociations) {
                viewModel.onClick(appWordAssociations)
            }
        })

        viewModel.events().observe(this, EventObserver { handle(it) })
        viewModel.configuredApps().observe(this, DataObserver { appsRecyclerView.updateApps(it) })
    }

    private fun handle(event: OpenConfigureAppEvent) {
        val intent = Intent(this@SkipperSettingsActivity, EditAppAssociationsActivity::class.java)
                .putExtra(EditAppAssociationsActivity.EXTRA_PACKAGE_NAME, event.app.packageName)
        startActivity(intent)
    }
}

private fun RecyclerView.updateApps(list: List<AppWordAssociations>) {
    (adapter as AppsAdapter).update(list)
}

data class OpenConfigureAppEvent(val app: AppPackageName)
