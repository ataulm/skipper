package com.ataulm.skipper

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_configure_apps.*
import java.util.*

class ConfiguredAppsActivity : AppCompatActivity() {

    private lateinit var viewModel: ConfigureAppsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configure_apps)
        appsRecyclerView.adapter = ConfiguredAppsAdapter(object : ConfiguredAppsAdapter.Callback {
            override fun onClick(configuredApp: ConfiguredApp) {
                viewModel.onClick(configuredApp)
            }
        })

        val repository = SkipperRepository(InstalledAppsService(packageManager), SharedPreferencesSkipperPersistence.create(this))
        viewModel = ConfigureAppsViewModel(repository)
    }

    override fun onResume() {
        super.onResume()
        viewModel.events().observe(this, Observer {
            if (it == null) {
                return@Observer
            }

            viewModel.onEventConsumed(it)
            val intent = Intent(this@ConfiguredAppsActivity, EditAppAssociationsActivity::class.java)
                    .putExtra(EditAppAssociationsActivity.EXTRA_PACKAGE_NAME, it.app.packageName)
            startActivity(intent)
        })
        viewModel.configuredApps().observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            (appsRecyclerView.adapter as ConfiguredAppsAdapter).update(it)
        })
    }
}

class ConfigureAppsViewModel(private val repository: SkipperRepository) : ViewModel() {

    private val navigationEventsLiveData = MutableLiveData<OpenConfigureAppEvent>()

    fun configuredApps(): LiveData<List<ConfiguredApp>> {
        val mediatorLiveData = MediatorLiveData<List<ConfiguredApp>>()
        mediatorLiveData.addSource(repository.appsToWordsMap(), { associations ->
            mediatorLiveData.addSource(repository.installedApps(), { apps ->
                mediatorLiveData.value = combine(associations!!, apps!!)
            })
        })
        return mediatorLiveData
    }

    // TODO: read up how to do this nicely, inc. sealed classes
    fun events(): LiveData<OpenConfigureAppEvent> {
        return navigationEventsLiveData
    }

    private fun combine(associations: AppsToWordsMap, apps: List<App>): List<ConfiguredApp> {
        return apps
                .sortedBy { app -> app.name.toLowerCase(Locale.US) }
                .map { ConfiguredApp(it, associations.getOrDefault(it.packageName, emptyList())) }
                .sortedBy { it.clickableWords.isEmpty() }
    }

    fun onClick(configuredApp: ConfiguredApp) {
        navigationEventsLiveData.value = OpenConfigureAppEvent(configuredApp.app.packageName)
    }

    fun onEventConsumed(it: OpenConfigureAppEvent) {
        // TODO: this is super wrong
        navigationEventsLiveData.value = null
    }
}

data class OpenConfigureAppEvent(val app: AppPackageName)
