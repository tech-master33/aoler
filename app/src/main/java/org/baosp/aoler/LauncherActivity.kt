package org.baosp.aoler

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import org.baosp.aoler.databinding.ActivityLauncherBinding
import kotlin.math.abs

/**
 * LauncherActivity — the home screen for aoler.
 *
 * Displays installed apps in a single scrollable column.
 * Swipe up anywhere on screen opens the full app drawer.
 * All interactive elements are accessibility-labelled for andrdscren compatibility.
 */
class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding
    private lateinit var gestureDetector: GestureDetector
    private val appAdapter = AppAdapter { appInfo -> launchApp(appInfo) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAppList()
        setupGestures()
    }

    override fun onResume() {
        super.onResume()
        loadInstalledApps()
    }

    private fun setupAppList() {
        binding.appList.apply {
            layoutManager = LinearLayoutManager(this@LauncherActivity)
            adapter = appAdapter
            // Announce list to screen reader when it appears
            contentDescription = getString(R.string.app_list_description)
        }
    }

    private fun setupGestures() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val diffY = (e2.y - (e1?.y ?: 0f))
                val diffX = (e2.x - (e1?.x ?: 0f))
                if (abs(diffY) > abs(diffX) &&
                    abs(diffY) > SWIPE_THRESHOLD &&
                    abs(velocityY) > SWIPE_VELOCITY_THRESHOLD
                ) {
                    if (diffY < 0) {
                        openAppDrawer()
                        return true
                    }
                }
                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    private fun loadInstalledApps() {
        val pm = packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val apps = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .map { resolveInfo ->
                AppInfo(
                    label = resolveInfo.loadLabel(pm).toString(),
                    packageName = resolveInfo.activityInfo.packageName,
                    activityName = resolveInfo.activityInfo.name,
                    icon = resolveInfo.loadIcon(pm)
                )
            }
            .filter { it.packageName != packageName } // exclude aoler itself
            .sortedBy { it.label.lowercase() }

        appAdapter.submitList(apps)
    }

    private fun launchApp(appInfo: AppInfo) {
        val intent = packageManager.getLaunchIntentForPackage(appInfo.packageName) ?: return
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    private fun openAppDrawer() {
        startActivity(Intent(this, AppDrawerActivity::class.java))
    }
}
