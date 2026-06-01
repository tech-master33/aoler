package org.baosp.aoler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.baosp.aoler.databinding.ItemAppBinding

/**
 * RecyclerView adapter for the app list.
 *
 * Each row is a full-width touch target — no small icons to tap.
 * The app name is set as the contentDescription so andrdscren reads it correctly.
 */
class AppAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppAdapter.AppViewHolder>(AppDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppViewHolder(
        private val binding: ItemAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(appInfo: AppInfo) {
            binding.appName.text = appInfo.label
            binding.appIcon.setImageDrawable(appInfo.icon)

            // Full row is the accessible element — not just the icon
            binding.root.contentDescription = appInfo.label
            binding.root.setOnClickListener { onAppClick(appInfo) }
        }
    }

    class AppDiffCallback : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo) =
            oldItem.packageName == newItem.packageName

        override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo) =
            oldItem.label == newItem.label && oldItem.packageName == newItem.packageName
    }
}
