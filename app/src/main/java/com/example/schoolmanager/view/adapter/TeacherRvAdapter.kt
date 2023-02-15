package com.example.schoolmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.schoolmanager.databinding.ItemTeacherBinding
import com.example.schoolmanager.model.network.Teacher

class TeacherRvAdapter :
    ListAdapter<Teacher, TeacherRvAdapter.TeacherItemViewHolder>(differCallback) {

    var itemClickListener: ((Teacher) -> Unit)? = null

    inner class TeacherItemViewHolder(val binding: ItemTeacherBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindViews(item: Teacher) {
            binding.teacherNameContentTextview.text = item.userNickName
            binding.teacherSchoolContentTextview.text = item.school
            binding.teacherSubjectContentTextview.text = item.subject
            binding.root.setOnClickListener {
                itemClickListener?.let { it(item) }
            }
            Glide.with(binding.root.context)
                .load(item.userProfileImageUrl)
                .into(binding.teacherCharacterImageview)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemTeacherBinding.inflate(inflater, parent, false)
        return TeacherItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TeacherItemViewHolder, position: Int) {
        val item = currentList[position]
        holder.bindViews(item)
    }

    fun setOnItemClickListener(listener: ((Teacher) -> Unit)) {
        itemClickListener = listener
    }

    companion object {
        val differCallback = object : DiffUtil.ItemCallback<Teacher>() {
            override fun areItemsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem.userId == newItem.userId
            }

            override fun areContentsTheSame(oldItem: Teacher, newItem: Teacher): Boolean {
                return oldItem == newItem
            }
        }
    }
}