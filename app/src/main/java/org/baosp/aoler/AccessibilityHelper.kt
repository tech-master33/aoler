package org.baosp.aoler

import android.content.Context
import android.view.View
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityManager

/**
 * AccessibilityHelper — utilities for integrating with andrdscren and TalkBack.
 *
 * Use these helpers instead of calling AccessibilityManager directly so that
 * announcements are consistent across the whole launcher.
 */
object AccessibilityHelper {

    /**
     * Announce a message to the screen reader.
     * Use for state changes that don't happen via focus (e.g. "3 results found").
     */
    fun announce(context: Context, message: String) {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
            ?: return
        if (!am.isEnabled) return

        val event = AccessibilityEvent.obtain(AccessibilityEvent.TYPE_ANNOUNCEMENT).apply {
            text.add(message)
            className = context.javaClass.name
            packageName = context.packageName
        }
        am.sendAccessibilityEvent(event)
    }

    /**
     * Mark a view as a live region so that content changes are automatically
     * announced by the screen reader without requiring focus.
     *
     * Use ACCESSIBILITY_LIVE_REGION_POLITE for non-critical updates (search results).
     * Use ACCESSIBILITY_LIVE_REGION_ASSERTIVE only for critical errors.
     */
    fun setLiveRegion(view: View, mode: Int = View.ACCESSIBILITY_LIVE_REGION_POLITE) {
        view.accessibilityLiveRegion = mode
    }

    /**
     * Returns true if any accessibility service is currently active.
     * Use to skip accessibility-only logic when no service is running.
     */
    fun isAccessibilityServiceActive(context: Context): Boolean {
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as? AccessibilityManager
        return am?.isEnabled == true
    }
}
