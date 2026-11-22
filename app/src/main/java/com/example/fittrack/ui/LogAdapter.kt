package com.example.fittrack.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fittrack.databinding.ItemLogBinding
import com.example.fittrack.util.prettyDate

data class LogRow(
    val id: Long, val title: String, val subtitle: String,
    val details: String, val date: String
)

class LogAdapter(
    private val onEdit: (Long) -> Unit,
    private val onDelete: (Long) -> Unit
) : ListAdapter<LogRow, LogAdapter.Holder>(Diff) {
    object Diff : DiffUtil.ItemCallback<LogRow>() {
        override fun areItemsTheSame(oldItem: LogRow, newItem: LogRow) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: LogRow, newItem: LogRow) = oldItem == newItem
    }

    class Holder(val binding: ItemLogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        ItemLogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = getItem(position)
        holder.binding.logTitle.text = item.title
        holder.binding.logSubtitle.text = item.subtitle
        holder.binding.logDetails.text = item.details
        holder.binding.logDate.text = prettyDate(item.date)
        holder.binding.editLog.setOnClickListener { onEdit(item.id) }
        holder.binding.deleteLog.setOnClickListener { onDelete(item.id) }
    }
}
