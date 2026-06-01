package org.baosp.aoler

import android.graphics.drawable.Drawable

/**
 * Represents a launchable app on the device.
 */
data class AppInfo(
    val label: String,
    val packageName: String,
    val activityName: String,
    val icon: Drawable
)
