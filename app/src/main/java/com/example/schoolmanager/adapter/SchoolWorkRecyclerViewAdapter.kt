package com.example.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.model.SchoolWork
import com.example.schoolmanager.R

class SchoolWorkRecyclerViewAdapter(
    val itemClickListener: (SchoolWork) -> Unit = {}
) :
    ListAdapter<SchoolWork, SchoolWorkRecyclerViewAdapter.SchoolWorksViewHolder>(differCallBack) {

    inner class SchoolWorksViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //활동제목
        private val schoolWorkUidTextView: TextView =
            itemView.findViewById(R.id.id_activity_number_textview)
        private val schoolWorkTitleTextView: TextView =
            itemView.findViewById(R.id.activity_title_textview)

        fun bindViews(schoolWork: SchoolWork) {
            schoolWorkUidTextView.text = schoolWork.toString()
            schoolWorkTitleTextView.text = schoolWork.schoolWorkTitle
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolWorksViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val schoolWorkItemView = inflater.inflate(R.layout.item_school_work, parent, false)
        return SchoolWorksViewHolder(schoolWorkItemView)
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
}

