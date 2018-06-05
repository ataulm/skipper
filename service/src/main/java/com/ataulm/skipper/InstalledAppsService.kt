package com.ataulm.skipper

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

class InstalledAppsService(private val packageManager: PackageManager) {

    fun getApps(): List<App> {
        val installedApps = mutableListOf<App>()
        for (resolveInfo in launchableActivities()) {
            val app = app(resolveInfo)
            if (app.packageName.packageName != BuildConfig.APPLICATION_ID) {
                installedApps.add(app)
            }
        }
        return installedApps
    }

    private fun launchableActivities(): List<ResolveInfo> {
        val activities = mutableListOf<ResolveInfo>()
        activities.addAll(getResolveInfos(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)))
        activities.addAll(getResolveInfos(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER)))
        return activities
    }

    private fun getResolveInfos(intent: Intent): List<ResolveInfo> {
        return packageManager.queryIntentActivities(intent, 0)
    }

    private fun app(resolveInfo: ResolveInfo): App {
        val applicationInfo = resolveInfo.activityInfo.applicationInfo
        val packageName = resolvePackageName(resolveInfo)

        val name = packageManager.getApplicationLabel(applicationInfo).toString()
        val icon = packageManager.getApplicationIcon(applicationInfo)

        return App(name, icon, AppPackageName(packageName))
    }

    private fun resolvePackageName(resolveInfo: ResolveInfo): String {
        return resolveInfo.activityInfo.packageName ?: resolveInfo.resolvePackageName
    }
}
