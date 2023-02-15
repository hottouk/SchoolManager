package com.example.schoolmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.databinding.ItemSubjectBinding

class SubjectRvAdapter : ListAdapter<String, SubjectRvAdapter.SubjectItemViewHolder>(differCallback) {
    var itemClickListener: ((String) -> Unit)? = null

    inner class SubjectItemViewHolder(val binding: ItemSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: String) {
            binding.subjectNameTextview.text = item
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectRvAdapter.SubjectItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemSubjectBinding.inflate(inflater, parent, false)
        return SubjectItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SubjectItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    fun setOnItemClickListener(listener: ((String) -> Unit)) {
        itemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                return oldItem == newItem
            }
        }
    }
}