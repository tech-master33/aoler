package org.baosp.aoler

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.baosp.aoler.databinding.ActivityAppDrawerBinding

/**
 * AppDrawerActivity — full alphabetical list of all installed apps with search.
 *
 * Accessible search field at the top: screen reader focuses it first.
 * Results update as you type — each update is announced via live region.
 */
class AppDrawerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAppDrawerBinding
    private val appAdapter = AppAdapter { appInfo -> launchApp(appInfo) }
    private var allApps: List<AppInfo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppDrawerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppList()
        setupSearch()
        loadAllApps()
    }

    private fun setupAppList() {
        binding.appList.apply {
            layoutManager = LinearLayoutManager(this@AppDrawerActivity)
            adapter = appAdapter
            contentDescription = getString(R.string.all_apps_list_description)
        }
    }

    private fun setupSearch() {
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterApps(s?.toString() ?: "")
            }
        })

        // Focus the search field immediately so screen reader users hear the prompt
        binding.searchField.requestFocus()
    }

    private fun loadAllApps() {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        allApps = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { resolveInfo ->
                AppInfo(
                    label = resolveInfo.loadLabel(pm).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    activityName = resolveInfo.activityInfo.name,
                    icon = resolveInfo.loadIcon(pm)
                )
            }
            .sortedBy { it.label.lowercase() }

        appAdapter.submitList(allApps)
    }

    private fun filterApps(query: String) {
        val filtered = if (query.isBlank()) {
            allApps
        } else {
            allApps.filter { it.label.contains(query, ignoreCase = true) }
        }
        appAdapter.submitList(filtered)

        // Announce result count for screen reader users
        binding.appList.contentDescription = resources.getQuantityString(
            R.plurals.search_results, filtered.size, filtered.size
        )
    }

    private fun launchApp(appInfo: AppInfo) {
        val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
