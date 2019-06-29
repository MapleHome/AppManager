package com.maple.appmanager.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.maple.appmanager.data.App
import com.maple.appmanager.databinding.ItemAppBinding

/**
 *
 * @author maple
 * @time 2019-06-29
 */
class AppListAdapter : ListAdapter<App, AppListAdapter.ViewHolder>(AppDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.apply {
            bind(createOnClickListener(item), item)
            itemView.tag = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemAppBinding.inflate(
                LayoutInflater.from(parent.context), parent, false))
    }

    private fun createOnClickListener(item: App): View.OnClickListener {
        return View.OnClickListener {
            Log.e("click ", "${item.appName}")
        }
    }

    class ViewHolder(
            private val binding: ItemAppBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(listener: View.OnClickListener, item: App) {
            binding.apply {
                clickListener = listener
                appBean = item
                executePendingBindings()
            }
        }
    }
}

private class AppDiffCallback : DiffUtil.ItemCallback<App>() {

    override fun areItemsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem.packageName == newItem.packageName
    }

    override fun areContentsTheSame(oldItem: App, newItem: App): Boolean {
        return oldItem == newItem
    }
}