package com.example.schoolmanager.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.network.SchoolWork
import com.example.schoolmanager.databinding.ItemSchoolWorkBinding

class SchoolWorkRecyclerViewAdapter() :
    ListAdapter<SchoolWork, SchoolWorkRecyclerViewAdapter.SchoolWorksViewHolder>(differCallBack) {

    var itemClickListener: ((SchoolWork) -> Unit)? = null

    inner class SchoolWorksViewHolder(private val binding: ItemSchoolWorkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindViews(schoolWork: SchoolWork) {
            with(binding) {
                schoolWorkTitleTextview.text = schoolWork.schoolWorkTitle
                schoolWorkSimpleInfoTextview.text = schoolWork.schoolWorkSimpleInfo
                totalScoreTextview.text = schoolWork.getTotalScore().toString()
                root.setOnClickListener {
                    itemClickListener?.let { it(schoolWork)}
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolWorksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = ItemSchoolWorkBinding.inflate(inflater, parent, false)
        return SchoolWorksViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SchoolWorksViewHolder, position: Int) {
        val schoolWork = currentList[position]
        holder.bindViews(schoolWork)
    }

    companion object {
        val differCallBack = object : DiffUtil.ItemCallback<SchoolWork>() {
            override fun areItemsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: SchoolWork, newItem: SchoolWork): Boolean {
                return oldItem == newItem
            }
        }
    }
    //외부 참조 함수
    fun setOnItemClickListener(listener: (SchoolWork) -> Unit) {
        itemClickListener = listener
    }
}

