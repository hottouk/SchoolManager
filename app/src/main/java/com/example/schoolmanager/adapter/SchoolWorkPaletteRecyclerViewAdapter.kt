package com.example.schoolmanager.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolmanager.R
import com.example.schoolmanager.model.SchoolWork

class SchoolWorkPaletteRecyclerViewAdapter(
    private val schoolWorkList: MutableList<SchoolWork> = mutableListOf()
) : RecyclerView.Adapter<SchoolWorkPaletteRecyclerViewAdapter.SchoolWorkPaletteHolder>()
    {
    inner class SchoolWorkPaletteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //활동제목
        private val schoolWorkTitleTextView: TextView =
            itemView.findViewById(R.id.schoolwork_title_viewpager2_textview)
        private val schoolWorkSimpleInfo: TextView =
            itemView.findViewById(R.id.school_work_info_viewpager2_textview)

        fun bindViews(schoolWork: SchoolWork) {
            schoolWorkTitleTextView.text = schoolWork.schoolWorkTitle
            schoolWorkSimpleInfo.text = schoolWork.schoolWorkSimpleInfo
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SchoolWorkPaletteHolder {
        val inflater = LayoutInflater.from(parent.context)
        val schoolWorkItemView = inflater.inflate(R.layout.item_school_work_palette, parent, false)
        return SchoolWorkPaletteHolder(schoolWorkItemView)
    }

    override fun onBindViewHolder(holder: SchoolWorkPaletteHolder, position: Int) {
        val schoolWork = schoolWorkList[position]
        holder.bindViews(schoolWork)

    }

    override fun getItemCount(): Int {
        return schoolWorkList.size
    }
}